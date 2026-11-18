package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.common.RedisIdGenerator;
import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.dto.aftersale.AftersaleDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersalePageQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleQueryDTO;
import com.xiangmu.youxuan.dto.aftersale.AftersaleUpdateDTO;
import com.xiangmu.youxuan.entity.Aftersale;
import com.xiangmu.youxuan.entity.Order;
import com.xiangmu.youxuan.entity.OrderItem;
import com.xiangmu.youxuan.mapper.AftersaleMapper;
import com.xiangmu.youxuan.mapper.OrderMapper;
import com.xiangmu.youxuan.mapper.ProductMapper;
import com.xiangmu.youxuan.service.AftersaleService;
import com.xiangmu.youxuan.vo.employeeVO.EAftersaleVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.userVO.UAftersaleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AftersaleServiceImpl implements AftersaleService {
    private final RedisIdGenerator redisIdGenerator;
    private final AftersaleMapper aftersaleMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private void checkAftersaleOwner(Aftersale aftersale) {
        if (!Objects.equals(
                aftersale.getUserId(),
                BaseContext.getCurrentId())) {
            throw new BusinessException("无权操作他人售后单");
        }
    }
    //新增售后（用户申请）
    @Transactional
    @Override
    public void insert(AftersaleDTO aftersaleDTO){
        aftersaleDTO.setUserId(BaseContext.getCurrentId());
        Order order = orderMapper.selectById(aftersaleDTO.getOrderId());
        if (order == null){
            throw new BusinessException("订单不存在");
        }
        //退款
        if (Objects.equals(aftersaleDTO.getType(), 0)) {
            Integer orderStatus = order.getStatus();
            if (orderStatus == null || (orderStatus != 1 && orderStatus != 2 && orderStatus != 3)) {
                throw new BusinessException(
                        "只有已支付订单可以申请退款");
            }
            aftersaleDTO.setOrderItemId(null);
            aftersaleDTO.setAmount(order.getPayAmount());
        }else {
            aftersaleDTO.setAmount(BigDecimal.ZERO);
        }
        if (!Objects.equals(
                order.getUserId(),
                BaseContext.getCurrentId())) {
            throw new BusinessException("不能为他人订单申请售后");
        }
        //同一订单同时只能有一个处理中的售后单（已取消status=5的不算）
        Long count = aftersaleMapper.selectByOrderIdAndStatus(aftersaleDTO.getOrderId());
        if (count != null && count > 0){
            throw new BusinessException("该订单已有处理中的售后申请");
        }
        Aftersale aftersale = new Aftersale();
        BeanUtils.copyProperties(aftersaleDTO,aftersale);
        long nextId = redisIdGenerator.nextId("aftersale");
        aftersale.setId(nextId);
        aftersale.setStatus(0);
        aftersaleMapper.insert(aftersale);
    }
    // 员工处理售后
    @Transactional
    @Override
    public void update(AftersaleUpdateDTO aftersaleUpdateDTO) {
        Aftersale aftersale = aftersaleMapper.selectById(aftersaleUpdateDTO.getId());
        if (aftersale == null) {
            throw new BusinessException("售后单不存在");
        }

        Integer currentStatus = aftersale.getStatus();
        Integer newStatus = aftersaleUpdateDTO.getStatus();
        Integer type = aftersale.getType();

        /*
         * 退款：0→1同意、0→2拒绝、1→4退款完成
         * 换货：0→1同意、0→2拒绝、1→4完成
         * 投诉：0→1同意、0→2拒绝、1→4完成
         */
        boolean allowed = (currentStatus == 0 && (newStatus == 1 || newStatus == 2)) || (currentStatus == 1 && newStatus == 4);

        if (!allowed) {
            throw new BusinessException("非法的售后状态推进");
        }

        Long currentEmployeeId = BaseContext.getCurrentId();

        // 首次处理：记录当前处理员工
        if (currentStatus == 0) {
            aftersaleUpdateDTO.setHandlerId(currentEmployeeId);
        }

        // 后续操作：只能由首次处理员工继续操作
        if (currentStatus == 1 && !Objects.equals(aftersale.getHandlerId(), currentEmployeeId)) {
            throw new BusinessException("该售后单由其他客服跟进，无法操作");
        }
        aftersaleUpdateDTO.setCurrentStatus(currentStatus);

        int rows = aftersaleMapper.update(aftersaleUpdateDTO);
        if (rows == 0) {
            throw new BusinessException("售后单状态已变化，请刷新后重试");
        }

        // 只有退款操作才退款、恢复库存
        if (Objects.equals(type, 0)
                && Objects.equals(newStatus, 4)) {
            int refundRows = orderMapper.refund(aftersale.getOrderId());
            if (refundRows == 0) {
                throw new BusinessException(
                        "订单状态不允许退款，请刷新后重试");
            }

            List<OrderItem> orderItems =
                    orderMapper.selectItemByOrderId(
                            aftersale.getOrderId());
            for (OrderItem orderItem : orderItems) {
                int stockRows = productMapper.updateStock(
                        orderItem.getProductId(), orderItem.getQuantity());

                if (stockRows != 1) {
                    throw new BusinessException(
                            orderItem.getProductName() + "库存归还失败");
                }

                int salesRows = productMapper.updateSales(
                        orderItem.getProductId(), -orderItem.getQuantity());

                if (salesRows != 1) {
                    throw new BusinessException(
                            orderItem.getProductName() + "销量扣回失败");
                }
            }
        }
    }
    //关闭售后单（取消售后）
    @Transactional
    @Override
    public void updateDelete(Long id){
        Aftersale aftersale = aftersaleMapper.selectById(id);
        if (aftersale == null){
            throw new BusinessException("售后单不存在");
        }
        String role = BaseContext.getCurrentRole();
        Long currentId = BaseContext.getCurrentId();
        if ("user".equals(role)
                && !Objects.equals(aftersale.getUserId(), currentId)) {
            throw new BusinessException("无权关闭他人的售后单");
        }
        if ("employee".equals(role)
                && !Objects.equals(aftersale.getHandlerId(), currentId)) {
            throw new BusinessException("你不是该售后单的处理人");
        }
        if (aftersale.getStatus() != 0 && aftersale.getStatus() != 1){
            throw new BusinessException("当前状态不可关闭");
        }
        aftersaleMapper.updateDelete(id);
    }
    // 用户确认投诉已解决
    @Transactional
    @Override
    public void confirmComplete(Long id) {
        Aftersale aftersale = aftersaleMapper.selectById(id);

        if (aftersale == null) {
            throw new BusinessException("售后单不存在");
        }

        if (!Objects.equals(
                aftersale.getUserId(),
                BaseContext.getCurrentId())) {
            throw new BusinessException("无权操作他人售后单");
        }

        // 只有投诉允许用户确认完成
        if (!Objects.equals(aftersale.getType(), 2)) {
            throw new BusinessException("只有投诉售后可由用户确认完成");
        }

        // 必须先由员工同意
        if (!Objects.equals(aftersale.getStatus(), 1)) {
            throw new BusinessException("当前售后状态不能确认完成");
        }

        AftersaleUpdateDTO updateDTO = new AftersaleUpdateDTO();
        updateDTO.setId(id);
        updateDTO.setStatus(4);
        updateDTO.setCurrentStatus(1);

        int rows = aftersaleMapper.update(updateDTO);
        if (rows == 0) {
            throw new BusinessException("售后单状态已变化，请刷新后重试");
        }
    }
    //员工分页查询售后单
    @Override
    public EPageQueryVO<EAftersaleVO> employeeSelect(
            AftersalePageQueryDTO aftersalePageQueryDTO){
        Integer begin = (aftersalePageQueryDTO.getPage()-1)*aftersalePageQueryDTO.getPageSize();
        Long count = aftersaleMapper.count(aftersalePageQueryDTO);
        List<Aftersale> aftersales = aftersaleMapper.select(begin,aftersalePageQueryDTO);
        List<EAftersaleVO> aftersaleVOS = new ArrayList<>();
        for (Aftersale aftersale : aftersales) {
            EAftersaleVO aftersaleVO = new EAftersaleVO();
            BeanUtils.copyProperties(aftersale,aftersaleVO);
            aftersaleVOS.add(aftersaleVO);
        }
        EPageQueryVO<EAftersaleVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setCount(count);
        ePageQueryVO.setPageQueryVO(aftersaleVOS);
        return ePageQueryVO;
    }

    //用户查询本人售后单
    @Override
    public List<UAftersaleVO> userSelectMy(){
        AftersaleQueryDTO query = new AftersaleQueryDTO();
        query.setUserId(BaseContext.getCurrentId());
        List<Aftersale> aftersales = aftersaleMapper.selectByQuery(query);
        List<UAftersaleVO> userAftersaleVOS =
                new ArrayList<>();
        for (Aftersale aftersale : aftersales) {
            UAftersaleVO userAftersaleVO =
                    new UAftersaleVO();
            BeanUtils.copyProperties(aftersale,userAftersaleVO);
            userAftersaleVOS.add(userAftersaleVO);
        }
        return userAftersaleVOS;
    }

}

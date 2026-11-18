package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.common.RedisIdGenerator;
import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.dto.order.OrderDTO;
import com.xiangmu.youxuan.dto.order.OrderItemDTO;
import com.xiangmu.youxuan.dto.order.OrderPageQueryDTO;
import com.xiangmu.youxuan.dto.order.OrderUpdateDTO;
import com.xiangmu.youxuan.entity.Order;
import com.xiangmu.youxuan.entity.OrderItem;
import com.xiangmu.youxuan.entity.Product;
import com.xiangmu.youxuan.mapper.OrderMapper;
import com.xiangmu.youxuan.mapper.ProductMapper;
import com.xiangmu.youxuan.service.OrderService;
import com.xiangmu.youxuan.vo.employeeVO.EOrderDetailVO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderItemVO;
import com.xiangmu.youxuan.vo.employeeVO.EOrderVO;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.userVO.UOrderDetailVO;
import com.xiangmu.youxuan.vo.userVO.UOrderItemVO;
import com.xiangmu.youxuan.vo.userVO.UOrderVO;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;
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
public class OrderServiceImpl implements OrderService {
    private final RedisIdGenerator redisIdGenerator;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    private void checkOrderOwner(Order order) {
        if (!Objects.equals(order.getUserId(),
                BaseContext.getCurrentId())) {
            throw new BusinessException("无权操作他人订单");
        }
    }

    //新增订单,同步扣减库存
    @Transactional
    @Override
    public void insert(OrderDTO orderDTO) {
        if (orderDTO.getOrderItemDTOS() == null || orderDTO.getOrderItemDTOS().isEmpty()) {
            throw new BusinessException("订单至少包含一个商品");
        }
        Long userId = BaseContext.getCurrentId();
        long nextId = redisIdGenerator.nextId("order");
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal payAmount = BigDecimal.ZERO;
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setId(nextId);
        order.setUserId(userId);
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItemDTOS()) {
            if (orderItemDTO == null || orderItemDTO.getProductId() == null) {
                throw new BusinessException("订单商品信息不能为空");
            }
            if (orderItemDTO.getQuantity() == null || orderItemDTO.getQuantity() <= 0) {
                throw new BusinessException("商品数量必须大于0");
            }
            Product product = productMapper.selectById(orderItemDTO.getProductId());
            if (product == null) {
                throw new BusinessException("商品不存在");
            }
            int stockRows = productMapper.updateStock(product.getId(), -orderItemDTO.getQuantity());
            if (stockRows != 1) {
                throw new BusinessException("商品库存不足或已下架");
            }
            BigDecimal payPrice = product.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()));
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setPayPrice(payPrice);
            payAmount = payAmount.add(payPrice);
            orderItems.add(orderItem);
        }
        order.setPayAmount(payAmount);
        orderMapper.insert(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            orderMapper.insertItem(orderItem);
        }
    }

    // 用户端：支付本人订单，增加销量
    @Transactional
    @Override
    public void pay(Long orderId) {
        Long currentUserId = BaseContext.getCurrentId();

        int payRows = orderMapper.pay(orderId, currentUserId);
        if (payRows != 1) {
            throw new BusinessException("订单不存在、无权操作或不是待支付状态");
        }

        List<OrderItem> orderItems = orderMapper.selectItemByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            int rows = productMapper.updateSales(
                    orderItem.getProductId(), orderItem.getQuantity());

            if (rows != 1) {
                throw new BusinessException(
                        orderItem.getProductName() + "商品销量更新失败");
            }
        }

    }

    //订单推进（状态+时间合并为一条 UPDATE）
    @Transactional
    @Override
    public void update(OrderUpdateDTO orderUpdateDTO) {
        Order order = orderMapper.selectById(orderUpdateDTO.getId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        Integer currentStatus = order.getStatus();
        Integer newStatus = orderUpdateDTO.getStatus();
        if (newStatus == null || (newStatus != 2 && newStatus != 3)) {
            throw new BusinessException("员工只能执行发货或完成订单");
        }
        if (currentStatus == 4) {
            throw new BusinessException("订单已取消");
        }
        if (currentStatus != newStatus - 1) {
            throw new BusinessException("订单状态只能逐级推进");
        }

        orderUpdateDTO.setCurrentStatus(currentStatus);
        int rows = orderMapper.updateStatus(orderUpdateDTO);
        if (rows == 0) {
            throw new BusinessException("订单状态已变化，请刷新后重试");
        }
    }

    // 用户端：取消本人的待支付订单
    @Transactional
    @Override
    public void updateDelete(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        checkOrderOwner(order);
        Integer status = order.getStatus();
        if (status == 4) {
            throw new BusinessException("订单已取消");
        }
        if (status == 3) {
            throw new BusinessException("订单已完成");
        }
        if (status == 1) {
            throw new BusinessException("订单已支付，请申请售后");
        }
        if (status == 2) {
            throw new BusinessException("订单已发货，请申请售后");
        }
        int rows = orderMapper.updateDelete(id);
        if (rows == 0) {
            throw new BusinessException("订单状态已变化，请刷新后重试");
        }
        // 查询这张订单的所有商品明细
        List<OrderItem> orderItems = orderMapper.selectItemByOrderId(id);

        for (OrderItem orderItem : orderItems) {
            // 传入正数，归还下单时预占的库存
            int stockRows = productMapper.updateStock(
                    orderItem.getProductId(), orderItem.getQuantity());

            if (stockRows != 1) {
                throw new BusinessException(
                        orderItem.getProductName() + "库存归还失败");
            }
        }
    }

    //员工分页查询订单
    @Transactional
    @Override
    public EPageQueryVO<EOrderVO> employeeSelect(OrderPageQueryDTO orderPageQueryDTO) {
        Integer page = orderPageQueryDTO.getPage();
        Integer pageSize = orderPageQueryDTO.getPageSize();
        long offset = (page - 1L) * pageSize;
        if (offset > Integer.MAX_VALUE) {
            throw new BusinessException("页码过大");
        }
        Integer begin = (int) offset;
        Long count = orderMapper.count(orderPageQueryDTO);
        List<Order> orders = orderMapper.select(begin, orderPageQueryDTO);
        List<EOrderVO> orderVOS = new ArrayList<>();
        for (Order order : orders) {
            EOrderVO orderVO = new EOrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOS.add(orderVO);
        }
        EPageQueryVO<EOrderVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setPageQueryVO(orderVOS);
        ePageQueryVO.setCount(count);
        return ePageQueryVO;
    }

    //用户列表查询本人订单
    @Transactional
    @Override
    public UQueryListVO<UOrderVO> userSelect(OrderPageQueryDTO orderPageQueryDTO) {
        Long userId = BaseContext.getCurrentId();
        orderPageQueryDTO.setUserId(userId);
        List<Order> orders = orderMapper.selectUserList(orderPageQueryDTO);
        List<UOrderVO> uOrderVOS = new ArrayList<>();
        for (Order order:orders){
            UOrderVO uOrderVO = new UOrderVO();
            BeanUtils.copyProperties(order,uOrderVO);
            uOrderVOS.add(uOrderVO);
        }
        UQueryListVO<UOrderVO> uOrderVOUQueryListVO = new UQueryListVO<>();
        uOrderVOUQueryListVO.setList(uOrderVOS);
        uOrderVOUQueryListVO.setCount((long)uOrderVOS.size());
        UQueryListVO<UOrderVO> u = uOrderVOUQueryListVO;
            UQueryListVO<UOrderVO> u2 = new UQueryListVO<>();
            if (orderPageQueryDTO.getStatus() != null) {
                if (orderPageQueryDTO.getPayMethod() != null) {
                    List<UOrderVO> u1 = new ArrayList<>();
                    for (UOrderVO uOrderVO : u.getList()) {
                        if (Objects.equals(uOrderVO.getStatus(), orderPageQueryDTO.getStatus()) && Objects.equals(uOrderVO.getPayMethod(), orderPageQueryDTO.getPayMethod())) {
                            u1.add(uOrderVO);
                        }
                    }
                    long count = u1.size();
                    u2.setList(u1);
                    u2.setCount(count);
                } else {
                    List<UOrderVO> u1 = new ArrayList<>();
                    for (UOrderVO uOrderVO : u.getList()) {
                        if (Objects.equals(uOrderVO.getStatus(), orderPageQueryDTO.getStatus())) {
                            u1.add(uOrderVO);
                        }
                    }
                    long count = u1.size();
                    u2.setList(u1);
                    u2.setCount(count);
                }
            } else {
                if (orderPageQueryDTO.getPayMethod() != null) {
                    List<UOrderVO> u1 = new ArrayList<>();
                    for (UOrderVO uOrderVO : u.getList()) {
                        if (Objects.equals(uOrderVO.getPayMethod(), orderPageQueryDTO.getPayMethod())) {
                            u1.add(uOrderVO);
                        }
                    }
                    long count = u1.size();
                    u2.setList(u1);
                    u2.setCount(count);
                } else {
                    u2 = u;
                }
            }
            return u2;
        }


    //员工根据id查询订单的完整信息和商品明细
    @Transactional
    @Override
    public EOrderDetailVO employeeSelectById(Long id){
        Order order = orderMapper.selectById(id);
        if (order == null){
            throw new BusinessException("订单不存在");
        }
        EOrderVO orderVO = new EOrderVO();
        BeanUtils.copyProperties(order,orderVO);
        List<OrderItem> orderItems = orderMapper.selectItemByOrderId(id);
        List<EOrderItemVO> itemVOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            EOrderItemVO itemVO = new EOrderItemVO();
            BeanUtils.copyProperties(orderItem,itemVO);
            itemVOS.add(itemVO);
        }
        EOrderDetailVO detailVO = new EOrderDetailVO();
        detailVO.setOrder(orderVO);
        detailVO.setItems(itemVOS);
        return detailVO;
    }

    //用户根据id查询本人订单的完整信息和商品明细
    @Transactional
    @Override
    public UOrderDetailVO userSelectById(Long id){
        Order order = orderMapper.selectUserById(id, BaseContext.getCurrentId());
        if (order == null){
            throw new BusinessException("订单不存在或无权查看");
        }
        UOrderVO userOrderVO = new UOrderVO();
        BeanUtils.copyProperties(order,userOrderVO);
        List<OrderItem> orderItems = orderMapper.selectItemByOrderId(id);
        List<UOrderItemVO> itemVOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            UOrderItemVO itemVO = new UOrderItemVO();
            BeanUtils.copyProperties(orderItem,itemVO);
            itemVOS.add(itemVO);
        }
        UOrderDetailVO detailVO = new UOrderDetailVO();
        detailVO.setOrder(userOrderVO);
        detailVO.setItems(itemVOS);
        return detailVO;
    }

}

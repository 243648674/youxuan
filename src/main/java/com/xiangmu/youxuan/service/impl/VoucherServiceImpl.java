package com.xiangmu.youxuan.service.impl;

import com.xiangmu.youxuan.common.BusinessException;
import com.xiangmu.youxuan.config.RabbitMqConfig;
import com.xiangmu.youxuan.context.BaseContext;
import com.xiangmu.youxuan.dto.voucher.UserVoucherQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherPageQueryDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherStatusDTO;
import com.xiangmu.youxuan.dto.voucher.VoucherUpdateDTO;
import com.xiangmu.youxuan.entity.Voucher;
import com.xiangmu.youxuan.mapper.VoucherMapper;
import com.xiangmu.youxuan.mq.VoucherReceiveMessage;
import com.xiangmu.youxuan.service.VoucherService;
import com.xiangmu.youxuan.service.cache.VoucherCache;
import com.xiangmu.youxuan.vo.employeeVO.EPageQueryVO;
import com.xiangmu.youxuan.vo.employeeVO.EVoucherVO;
import com.xiangmu.youxuan.vo.userVO.UQueryListVO;
import com.xiangmu.youxuan.vo.userVO.UUserVoucherVO;
import com.xiangmu.youxuan.vo.userVO.UVoucherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherMapper voucherMapper;
    private final VoucherCache voucherCache;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    //新增优惠券
    @Transactional
    @Override
    public void insert(VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.selectByTitle(voucherDTO.getTitle());
        if (voucher != null) {
            throw new BusinessException("优惠券已存在");
        }
        if (Objects.equals(voucherDTO.getType(), 0) && voucherDTO.getDiscountRate() != null) {
            throw new BusinessException("满减券不能设置折扣比例");
        }
        if (Objects.equals(voucherDTO.getType(), 1) && voucherDTO.getDiscountAmount() != null) {
            throw new BusinessException("打折券不能设置满减金额");
        }
        voucherMapper.insert(voucherDTO);
        String stockKey = "voucher:stock:" + voucherDTO.getId();
        redisTemplate.opsForValue().set(stockKey, voucherDTO.getStock());
    }

    //修改优惠券
    @Transactional
    @Override
    public void update(VoucherUpdateDTO voucherUpdateDTO) {
        Voucher voucher = voucherMapper.selectByIdForUpdate(voucherUpdateDTO.getId());
        if (voucher == null) {
            throw new BusinessException("优惠券不存在");
        }
        if (voucherUpdateDTO.getTitle() != null) {
            Voucher sameTitleVoucher = voucherMapper.selectByTitle(voucherUpdateDTO.getTitle());
            if (sameTitleVoucher != null
                    && !sameTitleVoucher.getId().equals(voucherUpdateDTO.getId())) {
                throw new BusinessException("优惠券已存在");
            }
        }
        if (Objects.equals(voucher.getType(), 0) && voucherUpdateDTO.getDiscountRate() != null) {
            throw new BusinessException("满减券不能设置折扣比例");
        }
        if (Objects.equals(voucher.getType(), 1) && voucherUpdateDTO.getDiscountAmount() != null) {
            throw new BusinessException("打折券不能设置满减金额");
        }
        voucherMapper.update(voucherUpdateDTO);
        if (voucherUpdateDTO.getStock() != null) {
            String stockKey = "voucher:stock:" + voucherUpdateDTO.getId();
            redisTemplate.opsForValue().set(stockKey, voucherUpdateDTO.getStock());
        }
    }

    //修改优惠券状态
    @Transactional
    @Override
    public void updateStatus(VoucherStatusDTO voucherStatusDTO) {
        Voucher voucher = voucherMapper.selectByIdForUpdate(voucherStatusDTO.getId());
        if (voucher == null) {
            throw new BusinessException("优惠券不存在");
        }
        voucherMapper.updateStatus(voucherStatusDTO.getId(), voucherStatusDTO.getStatus());
    }

    //删除已停用且没有领取记录的优惠券
    @Transactional
    @Override
    public void delete(Long id) {
        Long count = voucherMapper.countUserVoucherByVoucherId(id);
        if (count > 0) {
            throw new BusinessException("优惠券已有领取记录，不能删除");
        }
        int rows = voucherMapper.delete(id);
        if (rows != 1) {
            throw new BusinessException("优惠券不存在或未停用，不能删除");
        }
    }

    //员工分页查询优惠券
    @Override
    public EPageQueryVO<EVoucherVO> employeeSelect(
            VoucherPageQueryDTO voucherPageQueryDTO) {
        Long begin = (voucherPageQueryDTO.getPage() - 1L) * voucherPageQueryDTO.getPageSize();
        Long count = voucherMapper.count(voucherPageQueryDTO);
        List<Voucher> vouchers = voucherMapper.select(begin, voucherPageQueryDTO);
        List<EVoucherVO> voucherVOS = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            EVoucherVO voucherVO = new EVoucherVO();
            BeanUtils.copyProperties(voucher, voucherVO);
            voucherVOS.add(voucherVO);
        }
        EPageQueryVO<EVoucherVO> ePageQueryVO = new EPageQueryVO<>();
        ePageQueryVO.setCount(count);
        ePageQueryVO.setPageQueryVO(voucherVOS);
        return ePageQueryVO;
    }

    //员工根据ID查询优惠券完整信息
    @Override
    public EVoucherVO employeeSelectById(Long id) {
        Voucher voucher = voucherMapper.selectById(id);
        if (voucher == null) {
            throw new BusinessException("优惠券不存在");
        }
        EVoucherVO voucherVO = new EVoucherVO();
        BeanUtils.copyProperties(voucher, voucherVO);
        return voucherVO;
    }

    //用户查询当前可以免费领取的优惠券
    @Override
    public List<UVoucherVO> userListSelect() {
        List<Voucher> vouchers = voucherMapper.userListSelect();
        List<UVoucherVO> userVoucherVOS = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            UVoucherVO userVoucherVO = new UVoucherVO();
            BeanUtils.copyProperties(voucher, userVoucherVO);
            userVoucherVOS.add(userVoucherVO);
        }
        return userVoucherVOS;
    }

    //用户列表查询本人已领取的优惠券
    @Override
    public UQueryListVO<UUserVoucherVO> userSelectMy(UserVoucherQueryDTO userVoucherQueryDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BusinessException("请先登录");
        }
        List<UUserVoucherVO> userVoucherVOS =
                voucherMapper.selectUserList(userId, userVoucherQueryDTO);
        UQueryListVO<UUserVoucherVO> uQueryListVO = new UQueryListVO<>();
        uQueryListVO.setList(userVoucherVOS);
        uQueryListVO.setCount((long) userVoucherVOS.size());
        return uQueryListVO;
    }

    //用户领取优惠券库存预扣除（秒杀）
    @Override
    public void receive(Long voucherId) {
        String stockKey = "voucher:stock:" + voucherId;
        String lockKey = "voucher:receive:" + BaseContext.getCurrentId();
        String lockValue = UUID.randomUUID().toString();
        Long a = voucherCache.receive(stockKey, lockKey, lockValue);
        if (a == null) {
            throw new BusinessException("库存预扣结果未能确认");
        } else if (a == 0) {
            throw new BusinessException("库存不足，请稍后再试");
        } else if (a == 2) {
            throw new BusinessException("已有优惠券抢购，请稍后再试");
        } else if (a != 1) {
            throw new BusinessException("库存预扣失败");
        }
        VoucherReceiveMessage message = new VoucherReceiveMessage();
        message.setUserId(BaseContext.getCurrentId());
        message.setVoucherId(voucherId);
        message.setLockValue(lockValue);
        rabbitTemplate.convertAndSend(RabbitMqConfig.VOUCHER_RECEIVE_QUEUE,message);
    }

    @Override
    @Transactional
    public void createUserVoucher(Long userId,Long voucherId) {
        int row1 = voucherMapper.receiveStock(voucherId);
        if (row1 != 1) {
            throw new BusinessException("领取失败，请稍后再试");
        }
        int row2 = voucherMapper.insertUserVoucher(userId, voucherId);
        if (row2 != 1) {
            throw new BusinessException("领取失败，请稍后再试");
        }
    }
}


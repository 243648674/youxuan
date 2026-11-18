package com.xiangmu.youxuan.mq;

import com.xiangmu.youxuan.config.RabbitMqConfig;
import com.xiangmu.youxuan.service.VoucherService;
import com.xiangmu.youxuan.service.cache.VoucherCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoucherReceiveListener {
    private final VoucherCache voucherCache;
    private final VoucherService voucherService;

    @RabbitListener(queues = RabbitMqConfig.VOUCHER_RECEIVE_QUEUE)
    public void receive(VoucherReceiveMessage message) {
        Long userId = message.getUserId();
        Long voucherId = message.getVoucherId();
        String lockValue = message.getLockValue();
        if (userId == null || voucherId == null || lockValue == null) {
            throw new AmqpRejectAndDontRequeueException("领券消息参数不完整");
        }
        if (userId <= 0 || voucherId <= 0 || lockValue.isBlank()) {
            throw new AmqpRejectAndDontRequeueException("领券消息参数不正确");
        }
        voucherService.createUserVoucher(userId, voucherId);
        String lockKey = "voucher:receive:" + userId;
        try {
            voucherCache.unlock(lockKey, lockValue);
        } catch (RuntimeException e) {
            log.error("数据库发券已提交，但解锁失败，userId={}，voucherId={}",
                    userId, voucherId, e);
        }
    }
}

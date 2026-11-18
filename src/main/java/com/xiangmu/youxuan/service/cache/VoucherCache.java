package com.xiangmu.youxuan.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class VoucherCache {
    private final RedisTemplate<String,Object> redisTemplate;
    private static final DefaultRedisScript<Long> RECEIVE_SCRIPTY = new DefaultRedisScript<>();
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPTY = new DefaultRedisScript<>();
    static {
        RECEIVE_SCRIPTY.setLocation(new ClassPathResource("receive.lua"));
        RECEIVE_SCRIPTY.setResultType(Long.class);
    }
    static {
        UNLOCK_SCRIPTY.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPTY.setResultType(Long.class);
    }
    //用户领取优惠券时判断是否有库存，并获取锁
    public Long receive(String stockKey,String lockKey,String lockValue){
        return redisTemplate.execute(RECEIVE_SCRIPTY, Arrays.asList(stockKey,lockKey),lockValue);
    }
    public void unlock(String lockKey,String lockValue){
        redisTemplate.execute(UNLOCK_SCRIPTY, Collections.singletonList(lockKey),lockValue);
    }
}

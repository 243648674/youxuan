package com.xiangmu.youxuan.service.cache;

import com.xiangmu.youxuan.vo.userVO.UProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCache {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCT_CACHE_KEY = "youxuan:product:cache:";
    private static final Duration PRODUCT_CACHE_TTL = Duration.ofHours(24);
    private static final Duration PRODUCT_LOCK_TTL = Duration.ofSeconds(15);
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPTY = new DefaultRedisScript<>();
    static {
        UNLOCK_SCRIPTY.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPTY.setResultType(Long.class);
    }

    //写入分类对应的商品缓存
    public void setProductCache(String k,List<UProductVO> value){
        String key = PRODUCT_CACHE_KEY + k;
        long time = ThreadLocalRandom.current().nextInt(10);
        Duration TTL = PRODUCT_CACHE_TTL.plusHours(time);
        redisTemplate.opsForValue().set(key,value,TTL);
    }
    //读取分类对应的商品详情缓存
    public List<UProductVO> getProductCache(String k){
        String key = PRODUCT_CACHE_KEY + k;
        return (List<UProductVO>) redisTemplate.opsForValue().get(key);
    }
    //删除商品缓存
    public void deleteCategoryCache(String k){
        String key = PRODUCT_CACHE_KEY + k;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    redisTemplate.delete(key);
                }catch (DataAccessException e){
                    log.error("分类缓存删除失败，key={}", PRODUCT_CACHE_KEY, e);
                }
            }
        }
        );
    }
    public Boolean tryLock(String key,String value){
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key,value,PRODUCT_LOCK_TTL);
        return Boolean.TRUE.equals(b);
    }
    public void unLock(String lockKey,String lockValue){
        redisTemplate.execute(UNLOCK_SCRIPTY, Collections.singletonList(lockKey),lockValue);
    }
}

package com.xiangmu.youxuan.service.cache;

import com.xiangmu.youxuan.vo.userVO.UCategoryVO;
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
public class CategoryCache {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CATEGORY_CACHE_KEY = "youxuan:category:cache";
    private static final Duration CATEGORY_CACHE_TTL = Duration.ofHours(24);
    private static final Duration CATEGORY_LOCK_TTL = Duration.ofSeconds(15);
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>();
    static {
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }
    //写入分类缓存
    public void setCategoryCache(List<UCategoryVO> value){
        long time = ThreadLocalRandom.current().nextInt(10);
        Duration TTL = CATEGORY_CACHE_TTL.plusHours(time);
        redisTemplate.opsForValue().set(CATEGORY_CACHE_KEY,value,TTL);
    }
    //读取分类缓存
    public List<UCategoryVO> getCategoryCache(){
        return (List<UCategoryVO>) redisTemplate.opsForValue().get(CATEGORY_CACHE_KEY);
    }
    //删除分类缓存
    public void deleteCategoryCache(){
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    redisTemplate.delete(CATEGORY_CACHE_KEY);
                }catch (DataAccessException e){
                    log.error("分类缓存删除失败，key={}", CATEGORY_CACHE_KEY, e);
                }
            }
        }
        );
    }
    public Boolean tryLock(String key,String lockValue){
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key,lockValue,CATEGORY_LOCK_TTL);
        return Boolean.TRUE.equals(b);
    }
    public void unLock(String lockKey,String lockValue){
        redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey),lockValue);
    }
}

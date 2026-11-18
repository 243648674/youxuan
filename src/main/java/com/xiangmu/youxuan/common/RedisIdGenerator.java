package com.xiangmu.youxuan.common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisIdGenerator {
    private final StringRedisTemplate stringRedisTemplate;
    private static final long time = LocalDateTime.of(2026,1,1,0,0,0)
            .toEpochSecond(ZoneOffset.UTC);
    public long nextId(String keyPrefix){
        if (keyPrefix == null || keyPrefix.isEmpty()){
            throw new IllegalStateException("名字不正确");
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - time;
        if (timestamp > 2147483647 || timestamp < 0 ){
            throw new IllegalStateException("时间戳不正确");
        }
        String data = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String key = "id:counter:" + keyPrefix + ":" + data;
        Long nextId = stringRedisTemplate.opsForValue().increment(key);
        if (nextId == null) {
            throw new IllegalStateException("获取 ID 自增序号失败");
        }
        if (nextId > 4294967295L || nextId < 1){
            throw new IllegalStateException("时间戳不正确");
        }
        return (timestamp << 32) | nextId;
    }
}

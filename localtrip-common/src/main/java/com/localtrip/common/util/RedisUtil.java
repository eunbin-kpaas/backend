package com.localtrip.common.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 작업 유틸리티
 */
public class RedisUtil {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 문자열 값 저장
     */
    public void setString(String key, String value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }
    
    /**
     * 문자열 값 저장 (만료시간 포함)
     */
    public void setString(String key, String value, Duration duration) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, duration);
    }
    
    /**
     * 문자열 값 조회
     */
    public String getString(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object value = ops.get(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * 객체 저장
     */
    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 객체 저장 (만료시간 포함)
     */
    public void setObject(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }
    
    /**
     * 객체 조회
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? (T) value : null;
    }
    
    /**
     * 키 삭제
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 여러 키 삭제
     */
    public void delete(String... keys) {
        redisTemplate.delete(Set.of(keys));
    }
    
    /**
     * 키 존재 여부 확인
     */
    public boolean exists(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null && exists;
    }
    
    /**
     * 키 만료시간 설정
     */
    public void expire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }
    
    /**
     * 키 만료시간 조회 (초 단위)
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire != null ? expire : -1;
    }
    
    /**
     * 키 패턴으로 검색
     */
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    /**
     * 캐시 증가 (카운터)
     */
    public long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }
    
    /**
     * 캐시 증가 (지정된 값만큼)
     */
    public long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
    
    /**
     * 캐시 감소
     */
    public long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key, 1);
    }
    
    /**
     * 캐시 감소 (지정된 값만큼)
     */
    public long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}

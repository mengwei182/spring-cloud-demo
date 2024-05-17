package org.example.common.redis.service;

import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lihui
 * @since 2024/5/16
 */
@Getter
@Service
public class RedisService {
    public static final String HASH_KEY_PREFIX = "HASH_KEY_PREFIX";
    public static final String SEPARATOR = "###";
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Object get(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void put(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 为hash key设置过期时间，建议只在网络环境良好的情况下使用，因为该方法可能会因为网络原因导致hash key过期失败
     *
     * @param key
     * @param hashKey
     * @param value
     * @param duration
     * @see org.example.common.redis.listener.HashKeyExpirationEventMessageListener
     */
    public void put(String key, String hashKey, Object value, Duration duration) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.opsForValue().set(HASH_KEY_PREFIX + SEPARATOR + key + SEPARATOR + hashKey, "", duration);
    }

    /**
     * 为hash key设置过期时间，建议只在网络环境良好的情况下使用，因为该方法可能会因为网络原因导致hash key过期失败
     *
     * @param key
     * @param hashKey
     * @param value
     * @param timeout
     * @param timeUnit
     * @see org.example.common.redis.listener.HashKeyExpirationEventMessageListener
     */
    public void put(String key, String hashKey, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.opsForValue().set(HASH_KEY_PREFIX + SEPARATOR + key + SEPARATOR + hashKey, "", timeout, timeUnit);
    }

    public void putAll(String key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Boolean putIfAbsent(String key, String hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    public Map<Object, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void removeHash(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public List<Object> getSet(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    public void addSet(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public void addSet(String key, Duration duration, Object... values) {
        redisTemplate.opsForSet().add(key, values);
        redisTemplate.boundSetOps(key).expire(duration);
    }

    public void addSet(String key, long timeout, TimeUnit timeUnit, Object... values) {
        redisTemplate.opsForSet().add(key, values);
        redisTemplate.boundSetOps(key).expire(timeout, timeUnit);
    }

    public void removeSet(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    public Object getList(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public void addList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void addList(String key, Object value, Duration duration) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.boundListOps(key).expire(duration);
    }

    public void addList(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.boundListOps(key).expire(timeout, timeUnit);
    }

    public void removeList(String key, Object value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }
}
package org.example.common.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.common.redis.service.RedisService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * hash key的过期事件监听器
 *
 * @author lihui
 * @since 2024/5/17
 */
@Slf4j
public class HashKeyExpirationEventMessageListener extends KeyExpirationEventMessageListener {
    @Resource
    private RedisService redisService;

    public HashKeyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = redisService.getRedisTemplate().getStringSerializer().deserialize(message.getChannel());
        if (channel == null) {
            return;
        }
        // key过期事件
        String expireKey = redisService.getRedisTemplate().getStringSerializer().deserialize(message.getBody());
        if (expireKey == null) {
            return;
        }
        if (expireKey.startsWith(RedisService.HASH_KEY_PREFIX + RedisService.SEPARATOR)) {
            String[] split = expireKey.split(RedisService.SEPARATOR);
            String key = split[1];
            String hashKey = split[2];
            redisService.removeHash(key, hashKey);
            if (log.isDebugEnabled()) {
                log.debug("hash key expire,key:{},hash key:{}", key, hashKey);
            }
        }
    }
}
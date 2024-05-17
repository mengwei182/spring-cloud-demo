package org.example.common.redis.configuration;

import org.example.common.redis.Topic;
import org.example.common.redis.listener.HashKeyExpirationEventMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author lihui
 * @since 2023/4/25
 */
@Configuration
public class RedisMessageConfiguration {
    @Bean
    @ConditionalOnMissingBean(value = {RedisMessageListenerContainer.class, RedisConnectionFactory.class})
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }

    @Bean
    public HashKeyExpirationEventMessageListener hashKeyExpirationEventMessageListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        HashKeyExpirationEventMessageListener hashKeyExpirationEventMessageListener = new HashKeyExpirationEventMessageListener(redisMessageListenerContainer);
        // key过期监听通道
        redisMessageListenerContainer.addMessageListener(hashKeyExpirationEventMessageListener, new PatternTopic(Topic.KEY_EXPIRATION_CHANNEL));
        return hashKeyExpirationEventMessageListener;
    }
}
package org.example.mq.redis.configuration;

import org.example.mq.Topic;
import org.example.mq.redis.listener.ResourceMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author lihui
 * @since 2023/4/25
 */
@Configuration
public class RedisMessageConfiguration {
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, ResourceMessageListener resourceMessageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.addMessageListener(resourceMessageListener, new ChannelTopic(Topic.RedisTopic.REFRESH_RESOURCE_TOPIC));
        return redisMessageListenerContainer;
    }

    @Bean
    public ResourceMessageListener resourceMessageListener() {
        return new ResourceMessageListener();
    }
}
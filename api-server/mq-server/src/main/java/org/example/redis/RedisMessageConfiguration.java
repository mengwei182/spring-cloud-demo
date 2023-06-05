package org.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author lihui
 * @since 2023/4/25
 */
@Configuration
public class RedisMessageConfiguration {
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("refresh_resource_topic"));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(ResourceMessageReceiver resourceMessageReceiver) {
        return new MessageListenerAdapter(resourceMessageReceiver, "refreshResource");
    }

    @Bean
    public ResourceMessageReceiver resourceMessageReceiver() {
        return new ResourceMessageReceiver();
    }
}
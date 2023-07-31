package org.example.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lihui
 * @since 2022/11/15
 */
@Configuration
public class RedisConfiguration {
    @Bean
    @ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // String的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // Jackson2JsonRedisSerializer序列化类
        Jackson2JsonRedisSerializer<?> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(redisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //hash的value也采用jackson
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }
}
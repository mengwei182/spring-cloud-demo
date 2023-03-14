package org.example.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PrimaryKeyUtils {
    @Value("spring.application.name")
    private String applicationName;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * redis生成主键
     *
     * @return
     */
    public Long generatePrimaryKey() {
        return redisTemplate.opsForValue().increment(applicationName, 1);
    }
}
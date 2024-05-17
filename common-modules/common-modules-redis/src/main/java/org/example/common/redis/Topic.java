package org.example.common.redis;

/**
 * @author lihui
 * @since 2024/1/29
 */
public class Topic {
    public static final String REFRESH_RESOURCE_TOPIC = "topic_redis_refresh_resource";
    public static final String KEY_EXPIRATION_CHANNEL = "__keyevent@*__:expired";
}
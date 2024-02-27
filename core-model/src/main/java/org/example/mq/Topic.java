package org.example.mq;

/**
 * @author lihui
 * @since 2024/1/29
 */
public class Topic {
    public static class KafkaTopic {
        public static final String DEFAULT_TOPIC = "topics_default_topic";
    }

    public static class RedisTopic {
        public static final String REFRESH_RESOURCE_TOPIC = "topics_refresh_resource_topic";
    }
}
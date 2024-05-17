package org.example.common.kafka.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.common.core.configuration.ApplicationConfiguration;
import org.example.common.kafka.consumer.DefaultMessageConsumer;
import org.example.common.redis.service.RedisService;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注册到{@link DispatcherMessageListener#topics}中的消息都会经过该处理器分发。
 *
 * @author lihui
 * @since 2024/1/29
 */
@Data
@Slf4j
public class DispatcherMessageListener implements MessageListener<Object, Object> {
    private Set<String> topics;
    private AbstractMessageListener defaultMessageConsumer = new DefaultMessageConsumer();
    private Map<String, AbstractMessageListener> listeners = new HashMap<>();

    private AbstractMessageListener getMessageListener(ConsumerRecord<Object, Object> data) {
        if (data == null) {
            log.info("kafka consume message is null");
            return defaultMessageConsumer;
        }
        String msgId = data.topic() + "-" + data.partition() + "-" + data.offset();
        if (idempotent(data)) {
            log.info("rocketmq consume idempotence holds,message id is:{}", msgId);
            return defaultMessageConsumer;
        }
        AbstractMessageListener listener = listeners.get(data.topic());
        if (listener == null) {
            return defaultMessageConsumer;
        }
        return listener;
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data) {
        try {
            getMessageListener(data).onMessage(data);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment) {
        try {
            getMessageListener(data).onMessage(data);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        }
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, @NonNull Consumer<?, ?> consumer) {
        try {
            getMessageListener(data).onMessage(data, consumer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment, @NonNull Consumer<?, ?> consumer) {
        try {
            getMessageListener(data).onMessage(data, consumer);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        }
    }

    /**
     * 消息幂等校验，子类实现自己的判断逻辑，默认按照msgId字段值判断
     *
     * @param data
     * @return
     */
    public boolean idempotent(ConsumerRecord<Object, Object> data) {
        String msgId = data.topic() + "-" + data.partition() + "-" + data.offset();
        RedisService redisService = ApplicationConfiguration.getBean(RedisService.class);
        String value = (String) redisService.get(msgId);
        if (value == null) {
            // 如果msgId不存在（不存在或已过期），那么就设置一个较短的过期窗口时间：12s，12s内有相同的msgId就视为消息的重复投递
            // 为什么是12s，因为rocketmq关于消息重发的时间窗口是：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h，默认是10s
            // 我们的自定义窗口时间一定要比rocketmq的默认窗口时间要稍长一些（网络环境也会影响消息接收的时间），才能满足大多数情况下的消息重复校验
            redisService.set(msgId, msgId, Duration.ofSeconds(12L));
            return false;
        } else {
            // 如果msgId存在，说明在时间窗口内消息重复投递了
            return true;
        }
    }
}
package org.example.mq.kafka.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.mq.kafka.consumer.DefaultMessageConsumer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.Nullable;

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
        if (data == null || listeners.get(data.topic()) == null) {
            return defaultMessageConsumer;
        }
        return listeners.get(data.topic());
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data) {
        try {
            getMessageListener(data).onMessage(data);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment) {
        try {
            getMessageListener(data).onMessage(data, acknowledgment);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        }
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, Consumer<?, ?> consumer) {
        try {
            getMessageListener(data).onMessage(data, consumer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        try {
            getMessageListener(data).onMessage(data, acknowledgment, consumer);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        }
    }
}
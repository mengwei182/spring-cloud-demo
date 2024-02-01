package org.example.mq.kafka.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.mq.kafka.consumer.DefaultMessageConsumer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注册到{@link DispatcherMessageListener#topics}中的消息都会经过该处理器分发
 *
 * @author lihui
 * @since 2024/1/29
 */
@Data
@Slf4j
public class DispatcherMessageListener implements MessageListener<Object, Object> {
    private Set<String> topics;
    private AbstractMessageListener<Object, Object> defaultMessageConsumer = new DefaultMessageConsumer();
    private Map<String, AbstractMessageListener<Object, Object>> listeners = new HashMap<>();

    private AbstractMessageListener<Object, Object> getMessageListener(ConsumerRecord<Object, Object> data) {
        if (data == null || listeners.get(data.topic()) == null) {
            return defaultMessageConsumer;
        }
        return listeners.get(data.topic());
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data) {
        getMessageListener(data).onMessage(data);
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
        getMessageListener(data).onMessage(data, acknowledgment);
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, @NonNull Consumer<?, ?> consumer) {
        getMessageListener(data).onMessage(data, consumer);
    }

    @Override
    public void onMessage(@NonNull ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment, @NonNull Consumer<?, ?> consumer) {
        getMessageListener(data).onMessage(data, acknowledgment, consumer);
    }
}
package org.example.mq.kafka.listener;

import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.Nullable;

/**
 * @author lihui
 * @since 2024/1/31
 */
@Getter
public abstract class AbstractMessageListener {
    private final String topic;

    public AbstractMessageListener(String topic) {
        this.topic = topic;
    }

    public abstract void onMessage(ConsumerRecord<Object, Object> data);

    public abstract void onMessage(ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment);

    public abstract void onMessage(ConsumerRecord<Object, Object> data, Consumer<?, ?> consumer);

    public abstract void onMessage(ConsumerRecord<Object, Object> data, @Nullable Acknowledgment acknowledgment, Consumer<?, ?> consumer);
}
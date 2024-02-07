package org.example.mq.kafka.listener;

import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * 消息消费者的抽象类，提供模板方法。
 * <p>
 * 继承该抽象类并在构造方法中提供的topic会注册到{@link DispatcherMessageListener#setTopics(Set)}
 *
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
package org.example.common.kafka.listener;

import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

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

    /**
     * 消息消费逻辑
     *
     * @param data
     */
    public abstract void onMessage(ConsumerRecord<Object, Object> data);

    /**
     * 消息消费逻辑
     *
     * @param data
     * @param consumer
     */
    public abstract void onMessage(ConsumerRecord<Object, Object> data, Consumer<?, ?> consumer);

    /**
     * 获取message id的默认方法，用来校验消息幂等性，子类可以重写该方法
     *
     * @param data
     * @return
     */
    public String getMessageId(ConsumerRecord<Object, Object> data) {
        return data.topic() + "-" + data.partition() + "-" + data.offset();
    }
}
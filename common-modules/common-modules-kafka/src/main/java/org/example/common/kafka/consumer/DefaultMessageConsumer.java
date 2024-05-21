package org.example.common.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.common.kafka.Topic;
import org.example.common.kafka.listener.AbstractMessageListener;

/**
 * @author lihui
 * @since 2024/1/31
 */
@Slf4j
public class DefaultMessageConsumer extends AbstractMessageListener {
    public DefaultMessageConsumer() {
        super(Topic.DEFAULT_TOPIC);
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data) {
        log.info("kafka default message consumer message:{}", data.value());
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, Consumer<?, ?> consumer) {
        log.info("kafka default message consumer message:{}", data.value());
    }
}
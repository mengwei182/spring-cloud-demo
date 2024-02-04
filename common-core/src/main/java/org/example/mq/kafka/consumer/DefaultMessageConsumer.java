package org.example.mq.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.mq.Topic;
import org.example.mq.kafka.listener.AbstractMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author lihui
 * @since 2024/1/31
 */
@Slf4j
public class DefaultMessageConsumer extends AbstractMessageListener {
    public DefaultMessageConsumer() {
        super(Topic.KafkaTopic.DEFAULT_TOPIC);
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data) {
        log.info("data is null.");
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
        log.info("data is null.");
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, Consumer<?, ?> consumer) {
        log.info("data is null.");
    }

    @Override
    public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        log.info("data is null.");
    }
}
package org.example.common.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.common.rocketmq.Topic;
import org.example.common.rocketmq.listener.AbstractMessageListener;

import java.util.List;

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
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        for (MessageExt message : messages) {
            log.info("rocketmq default consumer,message is:{}", message.getMsgId());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
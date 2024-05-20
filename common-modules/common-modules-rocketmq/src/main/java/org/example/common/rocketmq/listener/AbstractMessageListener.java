package org.example.common.rocketmq.listener;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author lihui
 * @since 2024/1/31
 */
@Slf4j
@Getter
public abstract class AbstractMessageListener {
    private final String topic;

    public AbstractMessageListener(String topic) {
        this.topic = topic;
    }

    /**
     * 消息消费逻辑
     *
     * @param messages
     * @param context
     * @return
     */
    public abstract ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context);

    /**
     * 获取message id的默认方法，用来校验消息幂等性，子类可以重写该方法
     *
     * @param message
     * @param context
     * @return
     */
    public String getMessageId(MessageExt message, ConsumeConcurrentlyContext context) {
        return message.getMsgId();
    }
}
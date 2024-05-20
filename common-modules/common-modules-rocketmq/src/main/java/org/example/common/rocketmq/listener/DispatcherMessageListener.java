package org.example.common.rocketmq.listener;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.common.core.configuration.ApplicationConfiguration;
import org.example.common.redis.service.RedisService;
import org.example.common.rocketmq.consumer.DefaultMessageConsumer;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
public class DispatcherMessageListener implements MessageListenerConcurrently {
    private Set<String> topics;
    private AbstractMessageListener defaultMessageConsumer = new DefaultMessageConsumer();
    private Map<String, AbstractMessageListener> listeners = new HashMap<>();

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(messages)) {
            log.info("rocketmq consume message is null");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        log.info("rocketmq consume message:{}", JSON.toJSONString(messages));
        String topic = context.getMessageQueue().getTopic();
        try {
            AbstractMessageListener listener = listeners.get(topic);
            if (listener == null) {
                return defaultMessageConsumer.consumeMessage(messages, context);
            }
            List<MessageExt> messageList = new LinkedList<>();
            for (MessageExt message : messages) {
                byte[] bytes = message.getBody();
                // 空消息，直接跳过该条消息
                if (bytes == null) {
                    log.info("rocketmq consume invalid message,id is:{}", message.getMsgId());
                    continue;
                }
                // 出现幂等性，直接跳过该条消息
                if (idempotent(listener.getMessageId(message, context))) {
                    log.info("rocketmq consume idempotence holds,message id is:{}", message.getMsgId());
                    continue;
                }
                messageList.add(message);
            }
            return listener.consumeMessage(messageList, context);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 消息幂等校验，子类实现自己的判断逻辑，默认按照msgId字段值判断
     *
     * @param messageId
     * @return
     */
    public boolean idempotent(String messageId) {
        RedisService redisService = ApplicationConfiguration.getBean(RedisService.class);
        String value = (String) redisService.get(messageId);
        if (value == null) {
            // 如果msgId不存在（不存在或已过期），那么就设置一个较短的过期窗口时间：12s，12s内有相同的msgId就视为消息的重复投递
            // 为什么是12s，因为rocketmq关于消息重发的时间窗口是：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h，默认是10s
            // 我们的自定义窗口时间一定要比rocketmq的默认窗口时间要稍长一些（网络环境也会影响消息接收的时间），才能满足大多数情况下的消息重复校验
            redisService.set(messageId, messageId, Duration.ofSeconds(12L));
            return false;
        } else {
            // 如果msgId存在，说明在时间窗口内消息重复投递了
            return true;
        }
    }
}
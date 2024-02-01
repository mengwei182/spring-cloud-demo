package org.example.mq.kafka.configuration;

import org.example.configuration.ApplicationConfiguration;
import org.example.mq.kafka.consumer.DefaultMessageConsumer;
import org.example.mq.kafka.listener.AbstractMessageListener;
import org.example.mq.kafka.listener.DispatcherMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lihui
 * @since 2024/1/30
 */
@Configuration
public class KafkaConfiguration {
    @Bean
    @ConditionalOnClass(name = "org.springframework.kafka.core.ConsumerFactory")
    public KafkaMessageListenerContainer<Object, Object> kafkaMessageListenerContainer(ConsumerFactory<Object, Object> kafkaConsumerFactory, DispatcherMessageListener dispatcherMessageListener) {
        ContainerProperties containerProperties = new ContainerProperties(dispatcherMessageListener.getTopics().toArray(new String[]{}));
        containerProperties.setMessageListener(dispatcherMessageListener);
        containerProperties.setGroupId(DispatcherMessageListener.class.getName());
        return new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerProperties);
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.kafka.listener.KafkaMessageListenerContainer")
    public DispatcherMessageListener dispatcherMessageListener() {
        DispatcherMessageListener dispatcherMessageListener = new DispatcherMessageListener();
        Set<String> topics = new HashSet<>();
        Map<String, AbstractMessageListener<Object, Object>> listeners = new HashMap<>();
        Map<String, AbstractMessageListener> beansOfType = ApplicationConfiguration.getBeansOfType(AbstractMessageListener.class);
        for (String key : beansOfType.keySet()) {
            AbstractMessageListener<Object, Object> messageListener = beansOfType.get(key);
            String topic = messageListener.getTopic();
            topics.add(messageListener.getTopic());
            listeners.put(topic, messageListener);
        }
        dispatcherMessageListener.setTopics(topics);
        dispatcherMessageListener.setListeners(listeners);
        return dispatcherMessageListener;
    }

    @Bean
    @ConditionalOnBean(DispatcherMessageListener.class)
    public AbstractMessageListener<Object, Object> defaultMessageConsumer() {
        return new DefaultMessageConsumer();
    }
}
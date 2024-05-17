package org.example.common.kafka.configuration;

import org.example.common.core.configuration.ApplicationConfiguration;
import org.example.common.kafka.consumer.DefaultMessageConsumer;
import org.example.common.kafka.listener.AbstractMessageListener;
import org.example.common.kafka.listener.DispatcherMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    @ConditionalOnBean(KafkaMessageListenerContainer.class)
    public DispatcherMessageListener dispatcherMessageListener() {
        DispatcherMessageListener dispatcherMessageListener = new DispatcherMessageListener();
        Set<String> topics = new HashSet<>();
        Map<String, AbstractMessageListener> listeners = new HashMap<>();
        Map<String, AbstractMessageListener> beansOfType = ApplicationConfiguration.getBeansOfType(AbstractMessageListener.class);
        for (String key : beansOfType.keySet()) {
            AbstractMessageListener messageListener = beansOfType.get(key);
            String topic = messageListener.getTopic();
            topics.add(messageListener.getTopic());
            listeners.put(topic, messageListener);
        }
        dispatcherMessageListener.setTopics(topics);
        dispatcherMessageListener.setListeners(listeners);
        return dispatcherMessageListener;
    }

    @Bean
    @ConditionalOnBean(value = {ConsumerFactory.class, DispatcherMessageListener.class})
    public KafkaMessageListenerContainer<Object, Object> kafkaMessageListenerContainer(ConsumerFactory<Object, Object> kafkaConsumerFactory, DispatcherMessageListener dispatcherMessageListener) {
        ContainerProperties containerProperties = new ContainerProperties(dispatcherMessageListener.getTopics().toArray(new String[]{}));
        containerProperties.setMessageListener(dispatcherMessageListener);
        containerProperties.setGroupId(DispatcherMessageListener.class.getName());
        return new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerProperties);
    }

    @Bean
    @ConditionalOnBean(DispatcherMessageListener.class)
    public AbstractMessageListener defaultMessageConsumer() {
        return new DefaultMessageConsumer();
    }
}
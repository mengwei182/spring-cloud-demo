package org.example.system.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.common.global.GlobalVariable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

@Configuration
public class SystemKafkaConfiguration {
    @PostConstruct
    public void init() {
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
        NewTopic newTopic = new NewTopic(GlobalVariable.REFRESH_RESOURCE_TOPIC, 5, (short) 2);
        genericApplicationContext.registerBean(newTopic.name(), NewTopic.class, newTopic);
    }
}
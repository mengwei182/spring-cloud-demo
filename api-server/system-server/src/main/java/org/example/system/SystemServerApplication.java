package org.example.system;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.example.*")
public class SystemServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemServerApplication.class, args);
    }
}
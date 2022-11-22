package org.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.example.*")
public class UserServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }
}
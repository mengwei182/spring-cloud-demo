package org.example.system;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lihui
 * @since 2023/4/3
 */
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = "org.example")
@SpringBootApplication(scanBasePackages = "org.example")
public class AuthenticationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServerApplication.class, args);
    }
}
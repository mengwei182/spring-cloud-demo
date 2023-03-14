package org.example.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:common.properties")
public class CommonProperties {
    @Value("${url.white.list}")
    private String urlWhiteList;
}
package org.example.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@Configuration
@PropertySource("classpath:common.properties")
public class CommonProperties {
    @Value("${url.white.list}")
    private String urlWhiteList;
}
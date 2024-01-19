package org.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "common")
public class CommonProperties {
    private String skipUrl;
}
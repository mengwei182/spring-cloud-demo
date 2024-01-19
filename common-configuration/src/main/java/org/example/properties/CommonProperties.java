package org.example.properties;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Data
@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties {
    @NacosValue(value = "common.skip-url", autoRefreshed = true)
    private List<String> skipUrl;
}
package org.example.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:common.properties")
public class ConfigProperties {
   @Value("${no.auth.path}")
   private String noAuthUrls;
}
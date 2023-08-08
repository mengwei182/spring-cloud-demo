package org.example.system.start;

import org.example.common.util.RSAEncryptUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lihui
 * @since 2023/8/4
 */
@Component
public class ApplicationStarter implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RSAEncryptUtils.generatePublicPrivateKey();
    }
}
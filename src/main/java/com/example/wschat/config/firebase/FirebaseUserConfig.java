package com.example.wschat.config.firebase;

import com.example.wschat.model.config.AbstractFirebaseUserConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "ws.chat.firebase")
@PropertySource("classpath:config/firebase-config.properties")
public class FirebaseUserConfig extends AbstractFirebaseUserConfig {
}

package com.example.wschat.config.twilio;

import com.example.wschat.model.config.AbstractFirebaseUserConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
//@PropertySource("classpath:config/twilio.properties")
public class TwilioConfig extends AbstractFirebaseUserConfig {

//    public static final String TWILIO_ACCOUNT_SID = "ACd2b6a02da9c50afb6a0ffb580018f999";
//    public static final String TWILIO_AUTH_TOKEN = "0abdf8315d3190018b5f00086d9b8987";
//    public static final String TWILIO_PHONE_NUMBER = "+201010659903";

}

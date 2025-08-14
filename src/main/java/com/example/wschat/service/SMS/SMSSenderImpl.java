package com.example.wschat.service.SMS;

import com.example.wschat.model.entity.User;
import com.example.wschat.repository.jpa.UserJPARepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.wschat.config.twilio.TwilioConfig.*;

@Service
@AllArgsConstructor
@Slf4j
public class SMSSenderImpl implements SMSSender {

    private final UserJPARepository userJPARepository;

    @Override
    public void sendSMS(String senderMessagePhoneNumber,String receiverPhoneNumber) {

        System.out.println("In service of SMS the phone numbers are: " + senderMessagePhoneNumber + " " + receiverPhoneNumber);

        log.debug("Sending SMS to {}", receiverPhoneNumber);

        User user = userJPARepository.findByPhoneNumber(senderMessagePhoneNumber).orElseThrow(() -> new RuntimeException("User not found"));

        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        String messageBody = String.format(
                "Open Your Chat App, %s %s wants to talk with you! Emergency!!",
                user.getFirstName(),
                user.getLastName()
        );


        Message twilioMessage = Message.creator(
                new PhoneNumber(receiverPhoneNumber),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody
        ).create();


        log.info("SMS sent successfully to {}", receiverPhoneNumber);


    }
}

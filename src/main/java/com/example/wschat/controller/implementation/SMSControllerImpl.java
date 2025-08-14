package com.example.wschat.controller.implementation;

import com.example.wschat.controller.SMSController;
import com.example.wschat.service.SMS.SMSSender;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@AllArgsConstructor
public class SMSControllerImpl implements SMSController {

    private final SMSSender smsSender;

    @Override
    public ResponseEntity<Void> sendSMS(String senderPhoneNumber, String recipientPhoneNumber) {
        System.out.println("In Controller the phone numbers are: " + senderPhoneNumber + " " + recipientPhoneNumber);
        smsSender.sendSMS(senderPhoneNumber, recipientPhoneNumber);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}

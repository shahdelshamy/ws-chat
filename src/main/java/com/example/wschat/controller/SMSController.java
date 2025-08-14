package com.example.wschat.controller;

import com.example.wschat.model.dto.ChatMessageDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface SMSController {

    @MessageMapping("/notifications/SMS/{recipientPhoneNumber}")
    ResponseEntity<Void> sendSMS(@Payload String senderPhoneNumber, @DestinationVariable String recipientPhoneNumber);

}

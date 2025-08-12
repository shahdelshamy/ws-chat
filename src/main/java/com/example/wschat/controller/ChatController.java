package com.example.wschat.controller;

import com.example.wschat.model.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public interface ChatController {

    @MessageMapping("/chats/message")
    ResponseEntity<Void> send(@Payload ChatMessageDTO chatMessageDTO);

    @GetMapping("/chats/{senderPhoneNumber}/{recipientPhoneNumber}")
    ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(@PathVariable String senderPhoneNumber, @PathVariable String recipientPhoneNumber);

    @GetMapping("chats/{senderPhoneNumber}/{recipientPhoneNumber}/lastMessage")
    ResponseEntity<ChatMessageDTO> getLastUserMessage(@PathVariable String senderPhoneNumber, @PathVariable String recipientPhoneNumber);

    @MessageMapping("/chats/{senderPhoneNumber}/{recipientPhoneNumber}/messages/seen")
    ResponseEntity<Void> seeMessages(@DestinationVariable String senderPhoneNumber, @DestinationVariable String recipientPhoneNumber);

    @GetMapping("chats/{senderPhoneNumber}/{recipientPhoneNumber}/messages/unseen/count")
    ResponseEntity<Integer> countUnseenMessages(@PathVariable String senderPhoneNumber, @PathVariable String recipientPhoneNumber);


}

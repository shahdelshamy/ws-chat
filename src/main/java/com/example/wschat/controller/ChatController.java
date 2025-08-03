package com.example.wschat.controller;

import com.example.wschat.model.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public interface ChatController {

    @MessageMapping("/chat")
    ResponseEntity<Void> send(@Payload ChatMessageDTO chatMessageDTO);

    @GetMapping("/messages/{chatRoomId}")
    ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(@PathVariable String chatRoomId);
}

package com.example.wschat.controller.implementation;

import com.example.wschat.controller.ChatController;
import com.example.wschat.model.dto.ChatMessageDTO;
import com.example.wschat.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@AllArgsConstructor
public class ChatMessageControllerImpl implements ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public ResponseEntity<Void> send(ChatMessageDTO chatMessageDTO) {
        ChatMessageDTO chatMessage = chatMessageService.save(chatMessageDTO);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageDTO.getRecipient().getPhoneNumber(),  // username
                "/queue/messages",                               // destination
                chatMessageDTO);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(String senderPhoneNumber, String recipientPhoneNumber) {
        return ResponseEntity.ok(chatMessageService.findAllMessages(senderPhoneNumber , recipientPhoneNumber));
    }
}

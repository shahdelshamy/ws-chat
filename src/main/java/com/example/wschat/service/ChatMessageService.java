package com.example.wschat.service;

import com.example.wschat.model.dto.ChatMessageDTO;
import com.example.wschat.model.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessageDTO save(ChatMessageDTO chatMessage);

    List<ChatMessageDTO> findAllMessages(String senderPhoneNumber, String recipientPhoneNumber);

    ChatMessageDTO getLastUserMessage(String senderPhoneNumber, String recipientPhoneNumber);

}
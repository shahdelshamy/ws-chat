package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.ChatMessageDTO;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.ChatMessage;

import com.example.wschat.model.entity.User;
import com.example.wschat.repository.jpa.ChatMessageJPARepository;
import com.example.wschat.repository.jpa.UserJPARepository;
import com.example.wschat.service.ChatMessageService;
import com.example.wschat.service.ChatRoomService;
import com.example.wschat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageJPARepository chatMessageJPARepository;
    private final ChatRoomService chatRoomService;
    private final ChatMapper chatMapper;
    private final UserService userService;

    @Override
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {

        User sender = userService.getUserByPhoneNumber(chatMessageDTO.getSender().getPhoneNumber());

        User recipient = userService.getUserByPhoneNumber(chatMessageDTO.getRecipient().getPhoneNumber());

        ChatMessage chatMessage = chatMapper.toChatMessage(chatMessageDTO, sender, recipient);

        chatMessage.setIsSeen(false);

        ChatMessage chatMessageEntity = chatMessageJPARepository.save(chatMessage);

        return chatMapper.toChatMessageDTO(chatMessageEntity);
    }

    @Override
    public List<ChatMessageDTO> findAllMessages(String senderPhoneNumber, String recipientPhoneNumber) {

        User sender = userService.getUserByPhoneNumber(senderPhoneNumber);

        User recipient = userService.getUserByPhoneNumber(recipientPhoneNumber);

        List<ChatMessage> chatMessages = chatMessageJPARepository.findBySenderAndRecipient(sender, recipient);

        if (chatMessages != null && !chatMessages.isEmpty()) {
            return chatMessages.stream()
                    .map(chatMapper::toChatMessageDTO)
                    .toList();
        }
        return List.of();
    }

    @Override
    public ChatMessageDTO getLastUserMessage(String senderPhoneNumber, String recipientPhoneNumber) {

        User sender = userService.getUserByPhoneNumber(senderPhoneNumber);

        User recipient = userService.getUserByPhoneNumber(recipientPhoneNumber);

        ChatMessage chatMessages = chatMessageJPARepository.findLastMessage(sender, recipient);

        if (chatMessages != null) {
            return chatMapper.toChatMessageDTO(chatMessages);
        }

        return null;
    }

    @Override
    public void seeMessages(String senderPhoneNumber, String recipientPhoneNumber) {

        User sender = userService.getUserByPhoneNumber(senderPhoneNumber);

        User recipient = userService.getUserByPhoneNumber(recipientPhoneNumber);

        List<ChatMessage> chatMessages = chatMessageJPARepository.findNotSeenMessages(sender, recipient);

        chatMessages.forEach(
                (message)->{
                    message.setIsSeen(true);
                    chatMessageJPARepository.save(message);
                }

        );

    }


    @Override
    public Integer countUnseenMessages(String senderPhoneNumber, String recipientPhoneNumber) {

        User sender = userService.getUserByPhoneNumber(senderPhoneNumber);

        User recipient = userService.getUserByPhoneNumber(recipientPhoneNumber);

        List<ChatMessage> UnseenMessages = chatMessageJPARepository.findNotSeenMessages(sender, recipient);

        if (! UnseenMessages.isEmpty()) {
            return UnseenMessages.size();
        }

        return 0;
    }
}

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
    public ChatMessageDTO save(ChatMessageDTO chatMessage) {

        User sender = userService.getUserByPhoneNumber(chatMessage.getSender().getPhoneNumber());

        User recipient = userService.getUserByPhoneNumber(chatMessage.getRecipient().getPhoneNumber());

        ChatMessage chatMessageEntity = chatMessageJPARepository.save(chatMapper.toChatMessage(chatMessage, sender, recipient));

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


/*
    @Override
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {

        String chatRoomId = chatRoomService.createChatRoomOrGetId(chatMessageDTO.getSender(), chatMessageDTO.getRecipient());
        ChatMessage chatMessage = chatMapper.toChatMessage(chatMessageDTO, chatRoomId);
        chatMessageJPARepository.save(chatMessage);

        String senderPhoneNumber= chatMessage.getChatRoom().split("_")[0];
        String recipientPhoneNumber= chatMessage.getChatRoom().split("_")[1];

        return chatMapper.toChatMessageDTO(chatMessage, senderPhoneNumber, recipientPhoneNumber);
    }

    @Override
    public List<ChatMessageDTO> findAllByChatRoomId(String senderPhoneNumber, String recipientPhoneNumber) {

        UserDTO senderUserDto = UserDTO.builder()
                .phoneNumber(senderPhoneNumber)
                .build();

        UserDTO recipientUserDto = UserDTO.builder()
                .phoneNumber(recipientPhoneNumber)
                .build();


        String chatRoom = chatRoomService.createChatRoomOrGetId(senderUserDto, recipientUserDto);

        List<ChatMessage> chatMessages = chatMessageJPARepository.findByChatRoom(chatRoom);
        if (chatMessages != null && !chatMessages.isEmpty()) {
            return chatMessages.stream()
                    .map((chatMessage)-> {
                        String sender = chatRoom.split("_")[0];
                        String recipient = chatRoom.split("_")[1];
                        return chatMapper.toChatMessageDTO(chatMessage, sender, recipient);
                    })
                    .toList();
        }
        return List.of();
    }
     */
}

package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.ChatMessageDTO;
import com.example.wschat.model.entity.ChatMessage;
import com.example.wschat.model.entity.ChatRoom;
import com.example.wschat.repository.jpa.ChatMessageJPARepository;
import com.example.wschat.service.ChatMessageService;
import com.example.wschat.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageJPARepository chatMessageJPARepository;
    private final ChatRoomService chatRoomService;
    private final ChatMapper chatMapper;


    @Override
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {

        String chatRoomId = chatRoomService.createChatRoomOrGetId(chatMessageDTO.getSender(), chatMessageDTO.getRecipient());
        ChatMessage chatMessage = chatMapper.toChatMessage(chatMessageDTO, chatRoomId);
        chatMessageJPARepository.save(chatMessage);

        String sender= chatMessage.getChatRoom().split("_")[0];
        String recipient= chatMessage.getChatRoom().split("_")[1];

        return chatMapper.toChatMessageDTO(chatMessage, sender, recipient);
    }

    @Override
    public List<ChatMessageDTO> findAllByChatRoomId(String chatRoom) {
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
}

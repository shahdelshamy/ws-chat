package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.ChatRoom;
import com.example.wschat.model.entity.User;
import com.example.wschat.repository.jpa.ChatRoomJPARepository;
import com.example.wschat.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomJPARepository chatRoomJPARepository;
    private final ChatMapper chatMapper;

    @Override
    public String createChatRoomOrGetId(UserDTO sender, UserDTO recipient) {

        User senderUser = chatMapper.toUser(sender);
        User recipientUser = chatMapper.toUser(recipient);

        Optional<ChatRoom> chatRoom=chatRoomJPARepository.findBySenderIdAndRecipientId(
                senderUser, recipientUser
        );

        if (chatRoom.isPresent()) {
            return chatRoom.get().getChatRoom();
        }

        ChatRoom senderChatRoom = ChatRoom.builder()
                .chatRoom(senderUser.getFirstName() +"_"+ recipientUser.getFirstName())
                .sender(senderUser)
                .recipient(recipientUser)
                .build();

        ChatRoom recipientChatRoom = ChatRoom.builder()
                .chatRoom(senderUser.getFirstName() +"_"+ recipientUser.getFirstName())
                .sender(recipientUser)
                .recipient(senderUser)
                .build();

        chatRoomJPARepository.save(senderChatRoom);
        chatRoomJPARepository.save(recipientChatRoom);

        return senderUser.getFirstName() +"_"+ recipientUser.getFirstName();
    }

    @Override
    public ChatRoom getChatRoomId(UserDTO sender, UserDTO recipient) {

        return chatRoomJPARepository.findByChatRoom(
                sender.getFirstName() +"_"+ recipient.getFirstName()
        );
    }
}

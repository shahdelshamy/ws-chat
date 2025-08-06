package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.repository.jpa.UserJPARepository;
import com.example.wschat.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

//    private final ChatRoomJPARepository chatRoomJPARepository;
    private final UserJPARepository userJPARepository;
    private final ChatMapper chatMapper;

    @Override
    public String createChatRoomOrGetId(UserDTO sender, UserDTO recipient) {
        return "";
    }


/*
    @Override
    public String createChatRoomOrGetId(UserDTO sender, UserDTO recipient) {

        User senderUser = userJPARepository.findByPhoneNumber(sender.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User recipientUser = userJPARepository.findByPhoneNumber(recipient.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Optional<ChatRoom> chatRoom=chatRoomJPARepository.findBySenderAndRecipient(
                senderUser, recipientUser
        );

        if (chatRoom.isPresent()) {
            return chatRoom.get().getChatRoom();
        }

        ChatRoom senderChatRoom = ChatRoom.builder()
                .chatRoom(senderUser.getPhoneNumber() +"_"+ recipientUser.getPhoneNumber())
                .sender(senderUser)
                .recipient(recipientUser)
                .build();

        ChatRoom recipientChatRoom = ChatRoom.builder()
                .chatRoom(senderUser.getPhoneNumber() +"_"+ recipientUser.getPhoneNumber())
                .sender(recipientUser)
                .recipient(senderUser)
                .build();

        chatRoomJPARepository.save(senderChatRoom);
        chatRoomJPARepository.save(recipientChatRoom);

        return senderUser.getPhoneNumber() +"_"+ recipientUser.getPhoneNumber();
    }

    @Override
    public ChatRoom getChatRoomId(UserDTO sender, UserDTO recipient) {

        return chatRoomJPARepository.findByChatRoom(
                sender.getPhoneNumber() +"_"+ recipient.getPhoneNumber()
        );
    }

 */
}

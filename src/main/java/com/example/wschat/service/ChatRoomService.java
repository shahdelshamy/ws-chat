package com.example.wschat.service;

import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.ChatRoom;

public interface ChatRoomService {

    String createChatRoomOrGetId(UserDTO sender, UserDTO recipient);
    ChatRoom getChatRoomId(UserDTO sender, UserDTO recipient);
}

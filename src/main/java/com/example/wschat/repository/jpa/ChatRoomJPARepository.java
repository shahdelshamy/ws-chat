package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.ChatRoom;
import com.example.wschat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJPARepository extends JpaRepository<ChatRoom,Integer> {

    Optional<ChatRoom> findBySenderIdAndRecipientId(User user, User user1);

    ChatRoom findByChatRoom(String chatRoomId);
}

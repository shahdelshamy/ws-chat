package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageJPARepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByChatRoom(String chatRoom);
}

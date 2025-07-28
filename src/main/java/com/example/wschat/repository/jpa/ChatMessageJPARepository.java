package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJPARepository extends JpaRepository<ChatMessage,Long> {
}

package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.ChatMessage;
import com.example.wschat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageJPARepository extends JpaRepository<ChatMessage,Long> {
//    List<ChatMessage> findByChatRoom(String chatRoom);


    @Query(
    """
        SELECT m FROM ChatMessage m
        WHERE (m.sender = :sender AND m.recipient = :recipient)
           OR (m.sender = :recipient AND m.recipient = :sender)
        ORDER BY m.date ASC
    """
    )
    List<ChatMessage> findBySenderAndRecipient(User sender, User recipient);

    @Query(
            """
                SELECT m FROM ChatMessage m
                WHERE (m.sender = :sender AND m.recipient = :recipient)
                   OR (m.sender = :recipient AND m.recipient = :sender)
                ORDER BY m.date DESC LIMIT 1
            """
    )
    ChatMessage findLastMessage(User sender, User recipient);


    @Query(
            """
                SELECT m FROM ChatMessage m
                WHERE ((m.sender = :sender AND m.recipient = :recipient)
                   OR (m.sender = :recipient AND m.recipient = :sender))
                   AND m.isSeen = false
                ORDER BY m.date DESC
            """
    )
    List<ChatMessage> findNotSeenMessages(User sender, User recipient);



}

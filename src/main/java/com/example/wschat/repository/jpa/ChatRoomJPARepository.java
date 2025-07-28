package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomJPARepository extends JpaRepository<ChatRoom,Integer> {
}

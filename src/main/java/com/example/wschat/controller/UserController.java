package com.example.wschat.controller;


import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.vto.UserVTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public interface UserController {

    @MessageMapping("/user.add")
    @SendTo("/topic/public")
    ResponseEntity<UserVTO> addUser(@Payload UserDTO userDTO);  //from client to server, not a message mapping

    @MessageMapping("/user.disconnected")
    @SendTo("/topic/public")
    ResponseEntity<UserVTO> disConnectUser(@Payload UserDTO userDTO);

    @GetMapping("/users")
    ResponseEntity<List<UserVTO> >connectedUsers();    //from server to client, not a message mapping

}

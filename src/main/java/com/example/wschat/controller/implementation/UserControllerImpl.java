package com.example.wschat.controller.implementation;

import com.example.wschat.controller.UserController;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.vto.UserVTO;
import com.example.wschat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserVTO> addUser(UserDTO userDTO) {
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    @Override
    public ResponseEntity<UserVTO> disConnectUser(UserDTO userDTO) {
        return ResponseEntity.ok(userService.disConnectUser(userDTO));
    }

    @Override
    public ResponseEntity<List<UserVTO>> connectedUsers() {
        return ResponseEntity.ok(userService.getConnectedUsers());
    }
}

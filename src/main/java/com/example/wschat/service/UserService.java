package com.example.wschat.service;

import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.User;
import com.example.wschat.model.vto.UserVTO;

import java.util.List;

public interface UserService {

    UserVTO addUser(UserDTO userDTO);
    List<UserVTO> getAllUsers();
    UserVTO disConnectUser(UserDTO userDTO);
    User getUserByPhoneNumber(String phoneNumber);
}

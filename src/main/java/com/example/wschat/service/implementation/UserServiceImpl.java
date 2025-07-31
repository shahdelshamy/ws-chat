package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.User;
import com.example.wschat.model.enums.UserStatuses;
import com.example.wschat.model.vto.UserVTO;
import com.example.wschat.repository.jpa.UserJPARepository;
import com.example.wschat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJPARepository userJPARepository;
    private final ChatMapper chatMapper;

    @Override
    public UserVTO addUser(UserDTO userDTO) {
         Optional<User> user = userJPARepository.findByPhoneNumber(userDTO.getPhoneNumber());

         if(user.isPresent()){
             throw new RuntimeException("User already exists");
         }

         User userEntity = chatMapper.toUser(userDTO);
         userEntity.setStatus(UserStatuses.ONLINE);

        return chatMapper.toUserVTO(userEntity);
    }

    @Override
    public List<UserVTO> getConnectedUsers() {

        List<User> users = userJPARepository.findByStatus(UserStatuses.ONLINE);

        List<UserVTO> userVTOList =
                users.stream().map(user -> chatMapper.toUserVTO(user)).toList();

        return userVTOList;
    }

    @Override
    public UserVTO disConnectUser(UserDTO userDTO) {
        User user = chatMapper.toUser(userDTO);
        user.setStatus(UserStatuses.OFFLINE);
        user.setLastSeenOn(LocalDateTime.now());
        userJPARepository.save(user);

        return chatMapper.toUserVTO(user);
    }
}

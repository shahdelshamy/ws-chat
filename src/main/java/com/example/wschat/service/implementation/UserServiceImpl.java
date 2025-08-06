package com.example.wschat.service.implementation;

import com.example.wschat.mapper.ChatMapper;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.User;
import com.example.wschat.model.enums.UserStatuses;
import com.example.wschat.model.vto.UserVTO;
import com.example.wschat.repository.jpa.UserJPARepository;
import com.example.wschat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserJPARepository userJPARepository;
    private final ChatMapper chatMapper;

    @Override
    public UserVTO addUser(UserDTO userDTO) {

         Optional<User> user = userJPARepository.findByPhoneNumber(userDTO.getPhoneNumber());

         log.debug("User: {}",user.isPresent() ? user.get() : null);

         if(!user.isEmpty() && user.get().getStatus() == UserStatuses.OFFLINE) {
             User user1= user.get();
             user1.setStatus(UserStatuses.ONLINE);

             userJPARepository.save(user1);
             return chatMapper.toUserVTO(user1);
         }

        if(!user.isEmpty() && user.get().getStatus() == UserStatuses.ONLINE) {
            log.debug("User is already online: {}", user.get());
            return chatMapper.toUserVTO(user.get());
        }

         User userEntity = chatMapper.toUser(userDTO);
         userEntity.setStatus(UserStatuses.ONLINE);

        userEntity = userJPARepository.save(userEntity);

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
        Optional<User> userOptional = userJPARepository.findByPhoneNumber(userDTO.getPhoneNumber());

        if(userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        user.setStatus(UserStatuses.OFFLINE);
        user.setLastSeenOn(LocalDateTime.now());
        userJPARepository.save(user);

        return chatMapper.toUserVTO(user);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userJPARepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }
}

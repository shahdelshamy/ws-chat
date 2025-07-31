package com.example.wschat.mapper;

import com.example.wschat.model.dto.ChatMessageDTO;
import com.example.wschat.model.dto.UserDTO;
import com.example.wschat.model.entity.ChatMessage;
import com.example.wschat.model.entity.User;
import com.example.wschat.model.vto.UserVTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public abstract class ChatMapper {

    public abstract User toUser(UserDTO userDTO);

    public abstract UserVTO toUserVTO(User user);

    @Mapping(target = "chatRoom", source = "chatRoomId")
    public abstract ChatMessage toChatMessage(ChatMessageDTO chatMessageDTO, String chatRoomId) ;

    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "recipient", source = "recipient")
    public abstract ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage, String sender, String recipient) ;
}

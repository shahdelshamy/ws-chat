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

    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "content", source = "chatMessageDTO.content")
    @Mapping(target = "date", source = "chatMessageDTO.date")
    @Mapping(target = "id", ignore = true)
    public abstract ChatMessage toChatMessage(ChatMessageDTO chatMessageDTO , User sender, User recipient);

    /* <<<<<<<<<<<<<<  ? Windsurf Command ? >>>>>>>>>>>>>>>> */
    /**
     * Converts a ChatMessage entity to a ChatMessageDTO.
     *
     * @param chatMessageEntity the ChatMessage entity to convert
     * @return the converted ChatMessageDTO
     */

    /* <<<<<<<<<<  4dd435ce-78be-46b1-8c9f-609ecc3d041c  >>>>>>>>>>> */
    @Mapping(target = "date", source = "date")
    public abstract ChatMessageDTO toChatMessageDTO(ChatMessage chatMessageEntity) ;


//    @Mapping(target = "sender.phoneNumber", source = "senderPhoneNumber")
//    @Mapping(target = "recipient.phoneNumber", source = "recipientPhoneNumber")
//    public abstract ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage, String senderPhoneNumber, String recipientPhoneNumber) ;

}

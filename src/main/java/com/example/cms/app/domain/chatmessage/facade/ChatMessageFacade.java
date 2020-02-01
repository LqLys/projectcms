package com.example.cms.app.domain.chatmessage.facade;

import com.example.cms.app.domain.chatmessage.dto.ChatMessage;
import com.example.cms.app.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.app.domain.chatmessage.dto.SendChatMessageRequest;
import com.example.cms.app.domain.chatmessage.entity.ChatMessageEntity;
import com.example.cms.app.domain.chatmessage.service.ChatMessageService;
import com.example.cms.app.domain.travelgroup.service.TravelGroupService;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageFacade {

    private final ChatMessageService chatMessageService;
    private final TravelGroupService travelGroupService;
    private final UserService userService;

    public ChatMessageFacade(ChatMessageService chatMessageService, TravelGroupService travelGroupService, UserService userService) {
        this.chatMessageService = chatMessageService;
        this.travelGroupService = travelGroupService;
        this.userService = userService;
    }

    public List<ChatMessageDto> findAllByGroupId(Long groupId) {
        return chatMessageService.findAllByGroupId(groupId).stream()
                .map(message -> ChatMessageDto.builder()
                                .id(message.getId())
                                .author(message.getUser().getEmail())
                                .message(message.getMessage())
                                .time(message.getTime())
                                .firstName(message.getUser().getFirstName())
                                .lastName(message.getUser().getLastName())
                .build()).collect(Collectors.toList());
    }

    public void saveMessage(UserEntity authenticatedUser, SendChatMessageRequest sendChatMessageRequest) {

        ChatMessageEntity message = ChatMessageEntity.builder()
                .message(sendChatMessageRequest.getMessage())
                .time(LocalDateTime.now())
                .group(travelGroupService.findTravelGroup(sendChatMessageRequest.getGroupId()))
                .user(authenticatedUser)
                .build();

        chatMessageService.saveMessage(message);


    }
    public void saveMessage(ChatMessage chatMessage, Long groupId) {
        final UserEntity userByEmail = userService.findUserByEmail(chatMessage.getUser());
        ChatMessageEntity message = ChatMessageEntity.builder()
                .message(chatMessage.getValue())
                .time(LocalDateTime.now())
                .group(travelGroupService.findTravelGroup(groupId))
                .user(userByEmail)
                .build();

        chatMessageService.saveMessage(message);


    }
}

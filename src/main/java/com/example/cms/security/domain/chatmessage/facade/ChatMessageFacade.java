package com.example.cms.security.domain.chatmessage.facade;

import com.example.cms.security.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.security.domain.chatmessage.dto.SendChatMessageRequest;
import com.example.cms.security.domain.chatmessage.entity.ChatMessageEntity;
import com.example.cms.security.domain.chatmessage.service.ChatMessageService;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageFacade {

    private final ChatMessageService chatMessageService;
    private final TravelGroupService travelGroupService;

    public ChatMessageFacade(ChatMessageService chatMessageService, TravelGroupService travelGroupService) {
        this.chatMessageService = chatMessageService;
        this.travelGroupService = travelGroupService;
    }

    public List<ChatMessageDto> findAllByGroupId(Long groupId) {
        return chatMessageService.findAllByGroupId(groupId).stream()
                .map(message -> ChatMessageDto.builder()
                                .id(message.getId())
                                .author(message.getMessage())
                                .message(message.getMessage())
                                .time(message.getTime())
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
}

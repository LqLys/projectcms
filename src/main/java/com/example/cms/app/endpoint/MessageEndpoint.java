package com.example.cms.app.endpoint;

import com.example.cms.app.domain.chatmessage.dto.ChatMessage;
import com.example.cms.app.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.app.domain.chatmessage.facade.ChatMessageFacade;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/message")
public class MessageEndpoint {


    private final ChatMessageFacade chatMessageFacade;


    public MessageEndpoint(ChatMessageFacade chatMessageFacade) {
        this.chatMessageFacade = chatMessageFacade;
    }


    @GetMapping(path = "/group/{groupId}")
    public List<ChatMessageDto> getGroupMessages(@PathVariable("groupId") Long groupId) {
        return chatMessageFacade.findAllByGroupId(groupId);
    }

    @MessageMapping("/chat/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public ChatMessage get(@DestinationVariable("groupId") Long groupId, ChatMessage chatMessage) {
        chatMessageFacade.saveMessage(chatMessage, groupId);
        return chatMessage;
    }
}

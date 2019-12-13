package com.example.cms.security.endpoint;

import com.example.cms.security.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.security.domain.chatmessage.facade.ChatMessageFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

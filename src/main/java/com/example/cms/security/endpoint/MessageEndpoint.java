package com.example.cms.security.endpoint;

import com.example.cms.security.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.security.domain.chatmessage.facade.ChatMessageFacade;
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
    public List<ChatMessageDto> getGroupMessages(@PathVariable("groupId") Long groupId){
        return chatMessageFacade.findAllByGroupId(groupId);
    }
}

package com.example.cms.security.domain.chatmessage.controller;

import com.example.cms.security.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.security.domain.chatmessage.dto.SendChatMessageRequest;
import com.example.cms.security.domain.chatmessage.facade.ChatMessageFacade;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ChatMessageController {

    @Value("${api.base-url}")
    private String API_BASE;


    private final ChatMessageFacade chatMessageFacade;
    private final UserService userService;
    private final TravelGroupFacade travelGroupFacade;

    public ChatMessageController(ChatMessageFacade chatMessageFacade, UserService userService, TravelGroupFacade travelGroupFacade) {
        this.chatMessageFacade = chatMessageFacade;
        this.userService = userService;
        this.travelGroupFacade = travelGroupFacade;
    }


    @GetMapping(path = "/group/details/{groupId}/chat")
    public ModelAndView getGroupDetailsChat(@PathVariable("groupId") Long groupId, ModelAndView modelAndView){
        modelAndView.setViewName("group/groupDetailsChat");
        String groupName = travelGroupFacade.getTravelGroupById(groupId).getName();
        List<ChatMessageDto> allMessages = chatMessageFacade.findAllByGroupId(groupId);
        final String username = userService.getAuthenticatedUser().getEmail();
        modelAndView.addObject("newMessage", new SendChatMessageRequest());
        modelAndView.addObject("messages", allMessages);
        modelAndView.addObject("username", username);
        modelAndView.addObject("groupName", groupName);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("apiBase", API_BASE);
        return modelAndView;

    }

    @PostMapping("/chat/message")
    public ModelAndView sendMessage(@Valid SendChatMessageRequest sendChatMessageRequest, BindingResult bindingResult,
                                    ModelAndView modelAndView){
        modelAndView.setViewName("redirect:/group/details/" + sendChatMessageRequest.getGroupId()+ "/chat");
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        chatMessageFacade.saveMessage(authenticatedUser, sendChatMessageRequest);

        return modelAndView;
    }
}

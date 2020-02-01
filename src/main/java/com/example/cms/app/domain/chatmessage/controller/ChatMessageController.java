package com.example.cms.app.domain.chatmessage.controller;

import com.example.cms.app.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.app.domain.chatmessage.dto.SendChatMessageRequest;
import com.example.cms.app.domain.chatmessage.facade.ChatMessageFacade;
import com.example.cms.app.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
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

    @Value("${websocket.prefix}")
    private String WS_PREFIX;


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
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();
        final String username = authenticatedUser.getEmail();
        final String firstName = authenticatedUser.getFirstName();
        final String lastName = authenticatedUser.getLastName();
        modelAndView.addObject("newMessage", new SendChatMessageRequest());
        modelAndView.addObject("messages", allMessages);
        modelAndView.addObject("username", username);
        modelAndView.addObject("firstName", firstName);
        modelAndView.addObject("lastName", lastName);
        modelAndView.addObject("groupName", groupName);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("apiBase", API_BASE);
        modelAndView.addObject("WS_PREFIX", WS_PREFIX);
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

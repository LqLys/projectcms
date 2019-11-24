package com.example.cms.security.domain.chatmessage.controller;

import com.example.cms.security.domain.chatmessage.dto.ChatMessageDto;
import com.example.cms.security.domain.chatmessage.dto.SendChatMessageRequest;
import com.example.cms.security.domain.chatmessage.facade.ChatMessageFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
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


    private final ChatMessageFacade chatMessageFacade;
    private final UserService userService;

    public ChatMessageController(ChatMessageFacade chatMessageFacade, UserService userService) {
        this.chatMessageFacade = chatMessageFacade;
        this.userService = userService;
    }


    @GetMapping(path = "/group/details/{groupId}/chat")
    public ModelAndView getGroupDetailsChat(@PathVariable("groupId") Long groupId, ModelAndView modelAndView){
        modelAndView.setViewName("group/groupDetailsChat");

        List<ChatMessageDto> allMessages = chatMessageFacade.findAllByGroupId(groupId);
        modelAndView.addObject("newMessage", new SendChatMessageRequest());
        modelAndView.addObject("messages", allMessages);
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

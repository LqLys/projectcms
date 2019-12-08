package com.example.cms.security.domain.relation.controller;

import com.example.cms.security.domain.relation.dto.BaseFriendDto;
import com.example.cms.security.domain.relation.facade.RelationFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RelationController {

    private final RelationFacade relationFacade;
    private final UserService userService;

    public RelationController(RelationFacade relationFacade, UserService userService) {
        this.relationFacade = relationFacade;
        this.userService = userService;
    }


    @GetMapping(path = "/friends")
    public ModelAndView getFriends(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByEmail(auth.getName());

        List<BaseFriendDto> friends = relationFacade.getUserFriends(user.getId());
        modelAndView.addObject("friends",friends);
        modelAndView.setViewName("friend/friends");
        return modelAndView;
    }

}

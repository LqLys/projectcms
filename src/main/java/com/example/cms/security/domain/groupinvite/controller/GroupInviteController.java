package com.example.cms.security.domain.groupinvite.controller;

import com.example.cms.security.domain.groupinvite.facade.GroupInviteFacade;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class GroupInviteController {
    
    private final GroupInviteFacade groupInviteFacade;
    private final UserService userService;

    public GroupInviteController(GroupInviteFacade groupInviteFacade, UserService userService) {
        this.groupInviteFacade = groupInviteFacade;
        this.userService = userService;
    }

    @GetMapping("/notification/invitations")
    public ModelAndView getGroupInvitations(ModelAndView modelAndView){
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<GroupInviteDto> pendingInvites = groupInviteFacade.getUsersPendingInvites(authenticatedUser.getId());
        modelAndView.addObject("pendingInvites", pendingInvites);
        modelAndView.setViewName("/notification/invitations");
        return modelAndView;
    }
}

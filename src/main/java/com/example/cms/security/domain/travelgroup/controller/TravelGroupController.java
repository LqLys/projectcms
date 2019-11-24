package com.example.cms.security.domain.travelgroup.controller;

import com.example.cms.security.domain.groupinvite.facade.GroupInviteFacade;
import com.example.cms.security.domain.travelgroup.dto.*;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "/group")
public class TravelGroupController {

    private final TravelGroupFacade travelGroupFacade;
    private final GroupInviteFacade groupInviteFacade;
    private final UserService userService;

    public TravelGroupController(TravelGroupFacade travelGroupFacade,
                                 GroupInviteFacade groupInviteFacade,
                                 UserService userService) {
        this.travelGroupFacade = travelGroupFacade;
        this.groupInviteFacade = groupInviteFacade;
        this.userService = userService;
    }

    @GetMapping(path = "/groups")
    public ModelAndView getGroups(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByEmail(auth.getName());

        List<UserGroupsDto> travelGroups = travelGroupFacade.getUserGroupRoles(user.getId());
        List<GroupInviteDto> pendingInvites = groupInviteFacade.getUsersPendingInvites(user.getId());
        modelAndView.addObject("newTravelGroup", new CreateTravelGroupRequest());
        modelAndView.addObject("pendingInvites", pendingInvites);
        modelAndView.addObject("statusChangeRequest", new GroupInviteStatusChangeRequest());
        modelAndView.addObject("travelGroups", travelGroups);
        modelAndView.setViewName("group/groups");
        return modelAndView;
    }

    @PostMapping(path = "")
    public ModelAndView createGroup(@Valid CreateTravelGroupRequest createTravelGroupRequest,
                                    BindingResult bindingResult, ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.getAuthenticatedUser();
        travelGroupFacade.createTravelGroup(createTravelGroupRequest, user);
        modelAndView.setViewName("redirect:/group/groups");
        return modelAndView;
    }

    @GetMapping(path = "/details/{groupId}/members")
    public ModelAndView getGroupDetailMembers(@PathVariable("groupId") Long groupId, ModelAndView modelAndView) {
        List<GroupDetailsMembers> groupDetailsMembers = travelGroupFacade.getGroupDetailsMembers(groupId);
        modelAndView.addObject("groupDetailsMembers", groupDetailsMembers);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("groupInvitation", new GroupInviteRequest());
        modelAndView.setViewName("group/groupDetailsMembers");
        return modelAndView;

    }

    @PostMapping(path = "/invite")
    public ModelAndView sendInvitation(@Valid GroupInviteRequest groupInviteRequest, BindingResult bindingResult,
                                       ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        travelGroupFacade.sendInvitation(authenticatedUser, groupInviteRequest);
        modelAndView.setViewName("redirect:/group/groups");
        return modelAndView;
    }

    @PostMapping(path = "/invite/status")
    public ModelAndView changeInvitationStatus(@Valid GroupInviteStatusChangeRequest groupInviteStatusChangeRequest,
                                               BindingResult bindingResult, ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        Long groupId = groupInviteFacade.acceptInvitation(authenticatedUser, groupInviteStatusChangeRequest);
        modelAndView.setViewName("redirect:/group/details/"+groupId+"/members");
        return modelAndView;
    }



}

package com.example.cms.security.domain.travelgroup.controller;

import com.example.cms.security.domain.travelgroup.dto.CreateTravelGroupRequest;
import com.example.cms.security.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import io.swagger.models.Model;
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
    private final UserService userService;

    public TravelGroupController(TravelGroupFacade travelGroupFacade, UserService userService) {
        this.travelGroupFacade = travelGroupFacade;
        this.userService = userService;
    }

    @GetMapping(path = "/groups")
    public ModelAndView getGroups(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByEmail(auth.getName());

        List<UserGroupsDto> travelGroups = travelGroupFacade.getUserGroupRoles(user.getId());
        modelAndView.addObject("newTravelGroup", new CreateTravelGroupRequest());
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
        modelAndView.setViewName("group/groupDetailsMembers");
        return modelAndView;

    }


}

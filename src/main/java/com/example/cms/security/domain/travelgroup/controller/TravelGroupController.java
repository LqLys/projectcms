package com.example.cms.security.domain.travelgroup.controller;

import com.example.cms.security.domain.travelgroup.dto.CreateTravelGroupRequest;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

        List<TravelGroupDto> travelGroups = travelGroupFacade.getTravelGroups(user.getId());
        modelAndView.addObject("travelGroups", travelGroups);
        modelAndView.setViewName("group/groups");
        return modelAndView;
    }

    @PostMapping(path = "/group")
    public ModelAndView createGroup(@Valid CreateTravelGroupRequest createTravelGroupRequest,
                                    BindingResult bindingResult){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.getAuthenticatedUser();
        travelGroupFacade.createTravelGroup(createTravelGroupRequest, user);
        return new ModelAndView();
    }


}

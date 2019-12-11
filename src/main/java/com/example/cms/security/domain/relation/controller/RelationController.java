package com.example.cms.security.domain.relation.controller;

import com.example.cms.security.domain.relation.dto.AddFriendDto;
import com.example.cms.security.domain.relation.dto.BaseFriendDto;
import com.example.cms.security.domain.relation.dto.DeleteFriendDto;
import com.example.cms.security.domain.relation.facade.RelationFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
        UserEntity user = userService.getAuthenticatedUser();
        List<BaseFriendDto> friends = relationFacade.getUserFriends(user.getId());
        modelAndView.addObject("addFriendDto", new AddFriendDto());
        modelAndView.addObject("deleteFriendDto", new DeleteFriendDto());
        modelAndView.addObject("friends", friends);
        modelAndView.setViewName("friend/friends");
        return modelAndView;
    }

    @PostMapping(path = "/friends/add")
    public ModelAndView handleAddFriend(@Valid AddFriendDto addFriendDto, BindingResult bindingResult, ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        UserEntity friendToAdd = userService.findUserByEmail(addFriendDto.getEmail());
        if (friendToAdd == null){
           bindingResult.rejectValue("email", "error.addFriendDto", "user does not exist");
        }
        if(friendToAdd != null && relationFacade.isFriendAlready(authenticatedUser, friendToAdd.getId())){
            bindingResult.rejectValue("email", "error.addFriendDto", "user is already your friend");
        }
        if (bindingResult.hasErrors()) {
            List<BaseFriendDto> friends = relationFacade.getUserFriends(authenticatedUser.getId());
            modelAndView.addObject("friends", friends);
            modelAndView.setViewName("friend/friends");
        }else {
            relationFacade.addFriend(authenticatedUser, addFriendDto);
            modelAndView.setViewName("redirect:/friends");
        }
        return modelAndView;
    }

    @PostMapping(path = "/friends/delete")
    public ModelAndView handleDeleteFriend(DeleteFriendDto deleteFriendDto,  ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        relationFacade.deleteFriend(authenticatedUser, deleteFriendDto);
        modelAndView.setViewName("redirect:/friends");
        return modelAndView;
    }

//    ModelAndView modelAndView = new ModelAndView();
//    UserEntity userExists = userService.findUserByEmail(userEntity.getEmail());
//        if (userExists != null) {
//        bindingResult.rejectValue("email", "error.userEntity",
//                "There is already a user registered with the email provided");
//    }
//        if (bindingResult.hasErrors()) {
//        modelAndView.setViewName("registration");
//    } else {
//        userService.saveUser(userEntity);
//        modelAndView.addObject("successMessage", "User has been registered successfully");
//        modelAndView.addObject("userEntity", new UserEntity());
//        modelAndView.setViewName("registration");
//
//    }
//        return modelAndView;

}

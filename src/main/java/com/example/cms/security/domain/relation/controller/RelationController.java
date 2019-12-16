package com.example.cms.security.domain.relation.controller;

import com.example.cms.security.domain.relation.dto.*;
import com.example.cms.security.domain.relation.facade.RelationFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping(path = "/blocked")
    public ModelAndView getBlockedUsers(ModelAndView modelAndView) {
        UserEntity user = userService.getAuthenticatedUser();
        List<BaseBlockedUserDto> blockedUsers = relationFacade.getBlockedUsers(user.getId());
        modelAndView.addObject("blockUserDto", new BlockUserDto());
        modelAndView.addObject("unblockUserDto", new UnblockUserDto());
        modelAndView.addObject("blockedUsers", blockedUsers);
        modelAndView.setViewName("friend/blocked");
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
            List<BaseBlockedUserDto> blockedUsers = relationFacade.getBlockedUsers(authenticatedUser.getId());
            modelAndView.addObject("friends", friends);
            modelAndView.addObject("blockedUsers", blockedUsers);
            modelAndView.setViewName("friend/blocked");
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

    @PostMapping(path = "/blocked/add")
    public ModelAndView handleBlockUser(@Valid BlockUserDto blockUserDto, BindingResult bindingResult, ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        UserEntity uerToBlock = userService.findUserByEmail(blockUserDto.getEmail());
        if (uerToBlock == null){
            bindingResult.rejectValue("email", "error.addFriendDto", "user does not exist");
        }
        if(uerToBlock != null && relationFacade.isBlockedAlready(authenticatedUser, uerToBlock.getId())){
            bindingResult.rejectValue("email", "error.addFriendDto", "user is already blocked");
        }
        if (bindingResult.hasErrors()) {
            List<BaseBlockedUserDto> blockedUsers = relationFacade.getBlockedUsers(authenticatedUser.getId());
            modelAndView.addObject("blockedUsers", blockedUsers);
            modelAndView.setViewName("friend/blocked");
        }else {
            relationFacade.blockUser(authenticatedUser, blockUserDto);
            modelAndView.setViewName("redirect:/blocked");
        }
        return modelAndView;
    }

    @PostMapping(path = "/blocked/unblock")
    public ModelAndView handleUserUnblock(UnblockUserDto unblockUserDto,  ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        relationFacade.unblockUser(authenticatedUser, unblockUserDto);
        modelAndView.setViewName("redirect:/blocked");
        return modelAndView;
    }

}

package com.example.cms.security.domain.relation.facade;

import com.example.cms.security.domain.relation.dto.AddFriendDto;
import com.example.cms.security.domain.relation.dto.BaseFriendDto;
import com.example.cms.security.domain.relation.dto.DeleteFriendDto;
import com.example.cms.security.domain.relation.service.RelationService;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelationFacade {

    private final RelationService relationService;
    private final UserService userService;

    public RelationFacade(RelationService relationService, UserService userService) {
        this.relationService = relationService;
        this.userService = userService;
    }

    public List<BaseFriendDto> getUserFriends(Long userId) {
        return relationService.getFriends(userId).stream().map(this::mapToBaseFriend).collect(Collectors.toList());
    }

    private BaseFriendDto mapToBaseFriend(UserEntity userEntity) {
        return BaseFriendDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();

    }

    public void addFriend(UserEntity authenticatedUser, AddFriendDto addFriendDto) {
        UserEntity newFriend = userService.findUserByEmail(addFriendDto.getEmail());
        relationService.addFriend(authenticatedUser, newFriend);
    }

    public void deleteFriend(UserEntity authenticatedUser, DeleteFriendDto deleteFriendDto) {
        UserEntity friendToDelete = userService.findUserById(deleteFriendDto.getId());
        relationService.deleteFriend(authenticatedUser,friendToDelete);
    }

    public boolean isFriendAlready(UserEntity authenticatedUser, Long newFriendId) {
        return relationService.isFriendAlready(authenticatedUser, newFriendId);
    }
}

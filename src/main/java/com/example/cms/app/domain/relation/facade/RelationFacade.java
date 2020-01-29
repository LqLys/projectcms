package com.example.cms.app.domain.relation.facade;

import com.example.cms.app.domain.relation.dto.*;
import com.example.cms.app.domain.relation.service.RelationService;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
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
                .avatarUrl(userEntity.getAvatarUrl())
                .build();

    }

    private BaseBlockedUserDto mapToBlockedUser(UserEntity userEntity) {
        return BaseBlockedUserDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .avatarUrl(userEntity.getAvatarUrl())
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

    public boolean isBlockedAlready(UserEntity authenticatedUser, Long userToBlockId) {
        return relationService.isBlockedAlready(authenticatedUser, userToBlockId);

    }

    public List<BaseBlockedUserDto> getBlockedUsers(Long userId) {
        return relationService.getBlockedUsers(userId).stream().map(this::mapToBlockedUser).collect(Collectors.toList());
    }

    public void blockUser(UserEntity authenticatedUser, BlockUserDto blockUserDto) {
        UserEntity userToBlock = userService.findUserByEmail(blockUserDto.getEmail());
        relationService.blockUser(authenticatedUser, userToBlock);
    }

    public void unblockUser(UserEntity authenticatedUser, UnblockUserDto unblockUserDto) {
        UserEntity userToUnblock = userService.findUserById(unblockUserDto.getId());
        relationService.unblockUser(authenticatedUser,userToUnblock);
    }
}

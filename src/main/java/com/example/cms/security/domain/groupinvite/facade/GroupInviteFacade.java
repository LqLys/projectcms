package com.example.cms.security.domain.groupinvite.facade;

import com.example.cms.security.domain.groupinvite.entity.GroupInviteEntity;
import com.example.cms.security.domain.groupinvite.service.GroupInviteService;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteDto;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteStatusChangeRequest;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import com.example.cms.security.domain.usertravelgroup.entity.GroupRole;
import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupEntity;
import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupId;
import com.example.cms.security.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class GroupInviteFacade {

    private final GroupInviteService groupInviteService;
    private final UserTravelGroupService userTravelGroupService;
    private final UserService userService;

    public GroupInviteFacade(GroupInviteService groupInviteService, UserTravelGroupService userTravelGroupService, UserService userService) {
        this.groupInviteService = groupInviteService;
        this.userTravelGroupService = userTravelGroupService;
        this.userService = userService;
    }


    public List<GroupInviteDto> getUsersPendingInvites(Long userId){
        return groupInviteService.getUsersPendingInvites(userId);
    }
    @Transactional
    public Long acceptInvitation(UserEntity authenticatedUser, GroupInviteStatusChangeRequest groupInviteStatusChangeRequest) {
        GroupInviteEntity invitation = groupInviteService.getInvitation(groupInviteStatusChangeRequest.getId());
        TravelGroupEntity travelGroup = invitation.getTravelGroup();
        UserTravelGroupEntity userInGroup = UserTravelGroupEntity.builder()
                .groupRole(GroupRole.MEMBER)
                .id(new UserTravelGroupId(authenticatedUser.getId(), travelGroup.getId()))
                .build();
        userTravelGroupService.addUserToGroup(userInGroup);
        groupInviteService.delete(invitation);
        return travelGroup.getId();

    }

    public Long getNumberOfPendingInvitations(String username) {
        Long notifications = 0L;
        if(!username.equals("anonymousUser")){
            final UserEntity userByEmail = userService.findUserByEmail(username);
            List<GroupInviteDto> usersPendingInvites = getUsersPendingInvites(userByEmail.getId());
            notifications = Long.valueOf(usersPendingInvites.size());
        }
        return notifications;

    }
}

package com.example.cms.security.domain.groupinvite.facade;

import com.example.cms.security.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.security.domain.groupinvite.entity.GroupInviteEntity;
import com.example.cms.security.domain.groupinvite.service.GroupInviteService;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteDto;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteStatusChangeRequest;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
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

    public GroupInviteFacade(GroupInviteService groupInviteService, UserTravelGroupService userTravelGroupService) {
        this.groupInviteService = groupInviteService;
        this.userTravelGroupService = userTravelGroupService;
    }


    public List<GroupInviteDto> getUsersPendingInvites(Long userId){
        return groupInviteService.getUsersPendingInvites(userId);
    }
    @Transactional
    public Long acceptInvitation(UserEntity authenticatedUser, GroupInviteStatusChangeRequest groupInviteStatusChangeRequest) {
        GroupInviteEntity invitation = groupInviteService.getInvitation(groupInviteStatusChangeRequest.getId());
        invitation.setStatus(GroupInvitationStatus.ACCEPTED);
        TravelGroupEntity travelGroup = invitation.getTravelGroup();
        groupInviteService.save(invitation);
        UserTravelGroupEntity userInGroup = UserTravelGroupEntity.builder()
                .groupRole(GroupRole.MEMBER)
                .id(new UserTravelGroupId(authenticatedUser.getId(), travelGroup.getId()))
                .build();
        userTravelGroupService.addUserToGroup(userInGroup);
        return travelGroup.getId();

    }
}

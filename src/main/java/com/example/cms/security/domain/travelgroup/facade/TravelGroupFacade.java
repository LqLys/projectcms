package com.example.cms.security.domain.travelgroup.facade;

import com.example.cms.security.domain.groupinvite.service.GroupInviteService;
import com.example.cms.security.domain.travelgroup.dto.CreateTravelGroupRequest;
import com.example.cms.security.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteRequest;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.entity.GroupStatus;
import com.example.cms.security.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.mapper.TravelGroupMapper;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import com.example.cms.security.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelGroupFacade {


    private final TravelGroupService travelGroupService;
    private final TravelGroupMapper travelGroupMapper;
    private final UserTravelGroupService userTravelGroupService;
    private final UserService userService;
    private final GroupInviteService groupInviteService;

    public TravelGroupFacade(TravelGroupService travelGroupService, TravelGroupMapper travelGroupMapper, UserTravelGroupService userTravelGroupService, UserService userService, GroupInviteService groupInviteService) {
        this.travelGroupService = travelGroupService;
        this.travelGroupMapper = travelGroupMapper;
        this.userTravelGroupService = userTravelGroupService;
        this.userService = userService;
        this.groupInviteService = groupInviteService;
    }

    public TravelGroupDto getTravelGroupById(Long groupId) {
        return travelGroupMapper.map(travelGroupService.getTravelGroup(groupId),TravelGroupDto.class);
    }

    public void createTravelGroup(CreateTravelGroupRequest createTravelGroupRequest, UserEntity user) {

        TravelGroupEntity travelGroup = TravelGroupEntity.builder()
                .name(createTravelGroupRequest.getName())
                .groupStatus(GroupStatus.CREATED)
                .groupVisibility(GroupVisibility.PRIVATE)
                .build();
        travelGroupService.createTravelGroup(travelGroup);

        userTravelGroupService.createTravelGroup(user.getId(), travelGroup.getId());

    }

    public List<UserGroupsDto> getUserGroupRoles(Long userId){
        return travelGroupService.gerUserGroupRoles(userId);
    }

    public List<GroupDetailsMembers> getGroupDetailsMembers(Long groupId) {
        return travelGroupService.getGroupDetailsMembers(groupId);
    }

    public void sendInvitation(UserEntity authenticatedUser, GroupInviteRequest groupInviteRequest) {
        UserEntity invitationTarget = userService.findUserByEmail(groupInviteRequest.getEmail());
        TravelGroupEntity group = travelGroupService.findTravelGroup(groupInviteRequest.getGroupId());

        groupInviteService.sendInvitation(authenticatedUser, invitationTarget, group);

    }
}

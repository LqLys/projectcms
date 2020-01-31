package com.example.cms.app.domain.travelgroup.facade;

import com.example.cms.app.domain.expense.dto.ExpenseDto;
import com.example.cms.app.domain.expense.entity.ExpenseEntity;
import com.example.cms.app.domain.groupinvite.service.GroupInviteService;
import com.example.cms.app.domain.relation.service.RelationService;
import com.example.cms.app.domain.travelgroup.dto.*;
import com.example.cms.app.domain.travelgroup.entity.GroupStatus;
import com.example.cms.app.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.app.domain.travelgroup.mapper.TravelGroupMapper;
import com.example.cms.app.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.app.domain.travelgroup.service.TravelGroupService;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import com.example.cms.app.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TravelGroupFacade {


    private final TravelGroupService travelGroupService;
    private final TravelGroupMapper travelGroupMapper;
    private final UserTravelGroupService userTravelGroupService;
    private final UserService userService;
    private final GroupInviteService groupInviteService;
    private final RelationService relationService;

    public TravelGroupFacade(TravelGroupService travelGroupService, TravelGroupMapper travelGroupMapper, UserTravelGroupService userTravelGroupService, UserService userService, GroupInviteService groupInviteService, RelationService relationService) {
        this.travelGroupService = travelGroupService;
        this.travelGroupMapper = travelGroupMapper;
        this.userTravelGroupService = userTravelGroupService;
        this.userService = userService;
        this.groupInviteService = groupInviteService;
        this.relationService = relationService;
    }

    public TravelGroupDto getTravelGroupById(Long groupId) {
        return travelGroupMapper.map(travelGroupService.getTravelGroup(groupId), TravelGroupDto.class);
    }

    public TravelGroupDetailsDto getTravelGroupDetails(Long groupId) {
        return travelGroupMapper.map(travelGroupService.getTravelGroup(groupId), TravelGroupDetailsDto.class);
    }

    public void createTravelGroup(CreateTravelGroupRequest createTravelGroupRequest, UserEntity user) {

        TravelGroupEntity travelGroup = TravelGroupEntity.builder()
                .name(createTravelGroupRequest.getGroupName())
                .groupStatus(GroupStatus.CREATED)
                .groupVisibility(GroupVisibility.PRIVATE)
                .build();
        travelGroupService.createTravelGroup(travelGroup);

        userTravelGroupService.createTravelGroup(user.getId(), travelGroup.getId());
        createTravelGroupRequest.getFriendIds().stream().map(userService::findUserById)
                .forEach(u -> groupInviteService.sendInvitation(user, u, travelGroup));

    }

    public List<UserGroupsDto> getUserGroupRoles(Long userId) {
        return travelGroupService.gerUserGroupRoles(userId);
    }

    public List<GroupDetailsMembers> getGroupDetailsMembers(Long groupId) {
        return travelGroupService.getGroupDetailsMembers(groupId);
    }

    public void sendInvitation(UserEntity authenticatedUser, GroupInviteRequest groupInviteRequest) {
        UserEntity invitationTarget = userService.findUserByEmail(groupInviteRequest.getEmail());
        TravelGroupEntity group = travelGroupService.findTravelGroup(groupInviteRequest.getGroupId());
        if(invitationTarget != null) {
            boolean userAlreadyInGroup = userTravelGroupService.userAlreadyInGroup(invitationTarget.getId(), group.getId());
            if (!userAlreadyInGroup) {
                groupInviteService.sendInvitation(authenticatedUser, invitationTarget, group);
            }
        }
    }

    public TravelGroupExpensesDto getTravelGroupExpensesView(Long groupId) {
        TravelGroupEntity travelGroup = travelGroupService.getTravelGroup(groupId);
        List<ExpenseEntity> expenses = travelGroup.getExpenses();

        return TravelGroupExpensesDto.builder()
                .id(travelGroup.getId())
                .destination(travelGroup.getDestination())
                .name(travelGroup.getName())
                .expenses(expenses.stream().map(this::expenseToGroupExpensesView).collect(Collectors.toList()))
                .members(travelGroup.getUsers().stream().map(user -> new CreateDebtorDto(user.getId(), user.getEmail()))
                        .collect(Collectors.toList()))
                .build();
    }

    private ExpenseDto expenseToGroupExpensesView(ExpenseEntity expenseEntity) {
        return ExpenseDto.builder()
                .amount(expenseEntity.getAmount())
                .createdAt(expenseEntity.getCreatedAt())
                .createdBy(expenseEntity.getCreatedBy().getEmail())
                .id(expenseEntity.getId())
                .title(expenseEntity.getTitle())
                .build();
    }

    public void uninviteUser(GroupUninviteRequest groupUninviteRequest) {
        userTravelGroupService.uninviteUser(groupUninviteRequest);
    }

    public void inviteMultipleFriends(UserEntity authenticatedUser, InviteFriendsToGroupDto inviteFriendsToGroupDto) {
        TravelGroupEntity travelGroup = travelGroupService.findTravelGroup(inviteFriendsToGroupDto.getGroupId());

        inviteFriendsToGroupDto.getFriendIds().stream().map(userService::findUserById)
                .filter(u -> !userTravelGroupService.userAlreadyInGroup(u.getId(), travelGroup.getId()))
                .forEach(u -> groupInviteService.sendInvitation(authenticatedUser, u, travelGroup));


    }

    public boolean userAlreadyInGroup(Long userId, Long groupId) {
        return !userTravelGroupService.userAlreadyInGroup(userId, groupId);
    }

    public void editGroupDetails(TravelGroupDetailsDto travelGroupDetailsDto) {
        TravelGroupEntity travelGroup = travelGroupService.findTravelGroup(travelGroupDetailsDto.getGroupId());
        if (travelGroupDetailsDto.getName() != null) {
            travelGroup.setName(travelGroupDetailsDto.getName());
        }
        if (travelGroupDetailsDto.getDestination() != null) {
            travelGroup.setDestination(travelGroupDetailsDto.getDestination());
        }
        if (travelGroupDetailsDto.getStartDate() != null) {
            travelGroup.setStartDate(travelGroupDetailsDto.getStartDate());
        }
        if (travelGroupDetailsDto.getEndDate() != null) {
            travelGroup.setEndDate(travelGroupDetailsDto.getEndDate());
        }
        if (travelGroupDetailsDto.getGroupStatus() != null) {
            if (GroupStatus.CREATED == travelGroup.getGroupStatus() &&
                    GroupStatus.ACTIVE == travelGroupDetailsDto.getGroupStatus()) {
                relationService.addGroupMembersToFriends(travelGroup);
            }

            travelGroup.setGroupStatus(travelGroupDetailsDto.getGroupStatus());
        }
        if (travelGroupDetailsDto.getGroupVisibility() != null) {
            travelGroup.setGroupVisibility(travelGroupDetailsDto.getGroupVisibility());
        }
        if (travelGroupDetailsDto.getLat() != null) {
            travelGroup.setLat(travelGroupDetailsDto.getLat());
        }
        if (travelGroupDetailsDto.getLng() != null) {
            travelGroup.setLng(travelGroupDetailsDto.getLng());
        }
        if(travelGroupDetailsDto.getImgUrl() != null) {
            travelGroup.setImgUrl(travelGroupDetailsDto.getImgUrl());
        }

        travelGroupService.save(travelGroup);


    }

    public boolean viewerIsOrganizer(UserEntity authenticatedUser, Long travelGroupId) {
        return  userTravelGroupService.viewerIsOrganizer(authenticatedUser, travelGroupId);
    }

    public List<TravelGroupDto> getAllAvailableTravelGroups() {
        return travelGroupMapper.mapAsList(travelGroupService.getAllAvailableTravelGroups(), TravelGroupDto.class);
    }

    public void joinGroup(UserEntity authenticatedUser, Long groupId) {
        final TravelGroupEntity travelGroup = travelGroupService.findTravelGroup(groupId);
        userTravelGroupService.joinGroup(authenticatedUser, travelGroup);
    }
}

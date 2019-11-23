package com.example.cms.security.domain.travelgroup.facade;

import com.example.cms.security.domain.travelgroup.dto.CreateTravelGroupRequest;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.entity.GroupStatus;
import com.example.cms.security.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.mapper.TravelGroupMapper;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelGroupFacade {


    private final TravelGroupService travelGroupService;
    private final TravelGroupMapper travelGroupMapper;
    private final UserTravelGroupService userTravelGroupService;

    public TravelGroupFacade(TravelGroupService travelGroupService, TravelGroupMapper travelGroupMapper, UserTravelGroupService userTravelGroupService) {
        this.travelGroupService = travelGroupService;
        this.travelGroupMapper = travelGroupMapper;
        this.userTravelGroupService = userTravelGroupService;
    }

    public List<TravelGroupDto> getTravelGroups(Long id) {
        return travelGroupMapper.mapAsList(travelGroupService.getTravelGroups(id),TravelGroupDto.class);
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
}

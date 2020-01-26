package com.example.cms.app.domain.travelgroup.service;

import com.example.cms.app.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.app.domain.travelgroup.entity.GroupStatus;
import com.example.cms.app.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.app.domain.travelgroup.repository.TravelGroupRepository;
import com.example.cms.app.domain.travelgroup.repository.UserGroupsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelGroupService {
    
    private final TravelGroupRepository travelGroupRepository;

    public TravelGroupService(TravelGroupRepository travelGroupRepository) {
        this.travelGroupRepository = travelGroupRepository;
    }


    public TravelGroupEntity getTravelGroup(Long id) {
        return travelGroupRepository.getOne(id);
    }

    public TravelGroupEntity createTravelGroup(TravelGroupEntity travelGroup) {
        return travelGroupRepository.save(travelGroup);
    }

    public List<UserGroupsDto> gerUserGroupRoles(Long userId) {
        return travelGroupRepository.getUserGroupsByUserId(userId);
    }

    public List<GroupDetailsMembers> getGroupDetailsMembers(Long groupId) {
        return travelGroupRepository.getGroupDetailsMembers(groupId);
    }

    public TravelGroupEntity findTravelGroup(Long groupId) {
        return travelGroupRepository.getOne(groupId);
    }

    public void save(TravelGroupEntity travelGroup) {
        travelGroupRepository.save(travelGroup);
    }

    public List<TravelGroupEntity> getAllAvailableTravelGroups() {
        return travelGroupRepository.findAllByGroupStatusAndGroupVisibility(GroupStatus.CREATED, GroupVisibility.PUBLIC);
    }
}

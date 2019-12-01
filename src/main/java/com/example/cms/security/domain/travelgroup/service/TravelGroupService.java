package com.example.cms.security.domain.travelgroup.service;

import com.example.cms.security.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.repository.TravelGroupRepository;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

    public void createTravelGroup(TravelGroupEntity travelGroup) {
        travelGroupRepository.save(travelGroup);
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
}

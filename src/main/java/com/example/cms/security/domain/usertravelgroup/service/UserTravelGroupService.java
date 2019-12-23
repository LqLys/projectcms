package com.example.cms.security.domain.usertravelgroup.service;

import com.example.cms.security.domain.travelgroup.dto.GroupUninviteRequest;
import com.example.cms.security.domain.usertravelgroup.entity.GroupRole;
import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupEntity;
import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupId;
import com.example.cms.security.domain.usertravelgroup.repository.UserTravelGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class UserTravelGroupService {


    private final UserTravelGroupRepository userTravelGroupRepository;

    public UserTravelGroupService(UserTravelGroupRepository userTravelGroupRepository) {
        this.userTravelGroupRepository = userTravelGroupRepository;
    }


    public void createTravelGroup(Long userId, Long travelGroupId) {
        UserTravelGroupEntity userTravelGroup = UserTravelGroupEntity.builder()
                .id(new UserTravelGroupId(userId, travelGroupId))
                .groupRole(GroupRole.OWNER)
                .build();
        userTravelGroupRepository.save(userTravelGroup);
    }

    public UserTravelGroupEntity getUserTravelGroupByGroupId(Long groupId) {
        return userTravelGroupRepository.findById_TravelGroupId(groupId);
    }

    public void addUserToGroup(UserTravelGroupEntity userInGroup) {
        userTravelGroupRepository.save(userInGroup);
    }

    public void uninviteUser(GroupUninviteRequest request) {
        var userTravelGroup = userTravelGroupRepository.getOne(new UserTravelGroupId(request.getUserId(), request.getGroupId()));
        userTravelGroupRepository.delete(userTravelGroup);

    }

    public boolean userAlreadyInGroup(Long userId, Long groupId) {
        return userTravelGroupRepository.findById(new UserTravelGroupId(userId, groupId)).isPresent();
    }
}

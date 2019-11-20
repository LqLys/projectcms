package com.example.cms.security.domain.usertravelgroup.service;

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
}

package com.example.cms.app.domain.usertravelgroup.repository;

import com.example.cms.app.domain.usertravelgroup.entity.UserTravelGroupEntity;
import com.example.cms.app.domain.usertravelgroup.entity.UserTravelGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelGroupRepository extends JpaRepository<UserTravelGroupEntity, UserTravelGroupId> {

    UserTravelGroupEntity findById_TravelGroupId(Long groupId);
}

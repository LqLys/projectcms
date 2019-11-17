package com.example.cms.security.domain.usertravelgroup.repository;

import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupEntity;
import com.example.cms.security.domain.usertravelgroup.entity.UserTravelGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelGroupRepository extends JpaRepository<UserTravelGroupEntity, UserTravelGroupId> {
}

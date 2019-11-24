package com.example.cms.security.domain.travelgroup.repository.fragment;

import com.example.cms.security.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.security.domain.travelgroup.entity.QTravelGroupEntity;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.user.entity.QUserEntity;
import com.example.cms.security.domain.usertravelgroup.entity.QUserTravelGroupEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class TravelGroupRepositoryImpl implements TravelGroupRepositoryFragment {

    private final JPAQueryFactory jpaQueryFactory;

    public TravelGroupRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<UserGroupsDto> getUserGroupsByUserId(Long userId) {
        QUserEntity user = QUserEntity.userEntity;
        QUserTravelGroupEntity userTravelGroup = QUserTravelGroupEntity.userTravelGroupEntity;
        QTravelGroupEntity group = QTravelGroupEntity.travelGroupEntity;

        return jpaQueryFactory.select(
                Projections.constructor(UserGroupsDto.class, group.id, group.name,userTravelGroup.groupRole))
                .from(user)
                .join(userTravelGroup).on(user.id.eq(userTravelGroup.id.userId))
                .join(group).on(userTravelGroup.id.travelGroupId.eq(group.id))
                .where(userTravelGroup.id.userId.eq(userId))
                .fetch();

    }

    @Override
    public List<GroupDetailsMembers> getGroupDetailsMembers(Long groupId) {
        QUserEntity user = QUserEntity.userEntity;
        QUserTravelGroupEntity userTravelGroup = QUserTravelGroupEntity.userTravelGroupEntity;
        QTravelGroupEntity group = QTravelGroupEntity.travelGroupEntity;
        return jpaQueryFactory.select(
                Projections.constructor(GroupDetailsMembers.class, user.id, user.firstName, user.lastName, userTravelGroup.groupRole))
                .from(user)
                .join(userTravelGroup).on(user.id.eq(userTravelGroup.id.userId))
                .join(group).on(userTravelGroup.id.travelGroupId.eq(group.id))
                .where(group.id.eq(groupId))
                .fetch();

    }
}

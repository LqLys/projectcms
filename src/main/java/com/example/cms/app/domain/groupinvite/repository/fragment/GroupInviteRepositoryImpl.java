package com.example.cms.app.domain.groupinvite.repository.fragment;

import com.example.cms.app.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.app.domain.groupinvite.entity.QGroupInviteEntity;
import com.example.cms.app.domain.travelgroup.dto.GroupInviteDto;
import com.example.cms.app.domain.travelgroup.entity.QTravelGroupEntity;
import com.example.cms.app.domain.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class GroupInviteRepositoryImpl implements GroupInviteRepositoryFragment {

    private final JPAQueryFactory jpaQueryFactory;

    public GroupInviteRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<GroupInviteDto> getUsersInvites(Long targetId, GroupInvitationStatus groupInvitationStatus) {
        QUserEntity sourceUser = new QUserEntity("sourceUser");
        QUserEntity targetUser = new QUserEntity("targetUser");
        QTravelGroupEntity group = QTravelGroupEntity.travelGroupEntity;
        QGroupInviteEntity invite = QGroupInviteEntity.groupInviteEntity;

        return jpaQueryFactory.select(Projections.constructor(
                GroupInviteDto.class,
                invite.id,
                sourceUser.id,
                sourceUser.email,
                targetUser.id,
                group.id,
                group.name,
                invite.status))
                .from(sourceUser)
                .join(invite).on(sourceUser.id.eq(invite.invitationSource.id))
                .join(targetUser).on(invite.invitationTarget.id.eq(targetUser.id))
                .join(group).on(invite.travelGroup.id.eq(group.id))
                .where(targetUser.id.eq(targetId).and(invite.status.eq(groupInvitationStatus)))
                .distinct()
                .fetch();
    }
}

package com.example.cms.security.domain.relation.repository.fragment;

import com.example.cms.security.domain.relation.entity.QRelationEntity;
import com.example.cms.security.domain.relation.entity.RelationType;
import com.example.cms.security.domain.user.entity.QUserEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class RelationRepositoryImpl implements RelationRepositoryFragment {

    private final JPAQueryFactory jpaQueryFactory;

    public RelationRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<UserEntity> getFriends(Long userId) {
        QUserEntity sourceUser = new QUserEntity("sourceUser");
        QUserEntity targetUser = new QUserEntity("targetUser");
        QRelationEntity relation = QRelationEntity.relationEntity;

        return jpaQueryFactory.select(targetUser).from(sourceUser)
                .join(relation).on(sourceUser.id.eq(relation.source.id))
                .join(targetUser).on(relation.target.id.eq(targetUser.id))
                .where(relation.relationType.eq(RelationType.FRIEND))
                .fetch();
    }
}

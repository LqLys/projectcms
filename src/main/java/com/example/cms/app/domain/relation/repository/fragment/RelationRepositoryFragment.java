package com.example.cms.app.domain.relation.repository.fragment;

import com.example.cms.app.domain.user.entity.UserEntity;

import java.util.List;

public interface RelationRepositoryFragment {

    List<UserEntity> getFriends(Long userId);

    List<UserEntity> getBlockedUsers(Long userId);
}

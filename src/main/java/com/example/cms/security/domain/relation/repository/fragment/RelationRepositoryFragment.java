package com.example.cms.security.domain.relation.repository.fragment;

import com.example.cms.security.domain.user.entity.UserEntity;

import java.util.List;

public interface RelationRepositoryFragment {

    List<UserEntity> getFriends(Long userId);
}

package com.example.cms.security.domain.relation.service;

import com.example.cms.security.domain.relation.repository.RelationRepository;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {

    private final RelationRepository relationRepository;

    public RelationService(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }


    public List<UserEntity> getFriends(Long userId){
        return relationRepository.getFriends(userId);
    }
}

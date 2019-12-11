package com.example.cms.security.domain.relation.service;

import com.example.cms.security.domain.relation.dto.AddFriendDto;
import com.example.cms.security.domain.relation.entity.RelationEntity;
import com.example.cms.security.domain.relation.entity.RelationType;
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

    public void addFriend(UserEntity authenticatedUser, UserEntity newFriend) {

        RelationEntity relation = RelationEntity.builder()
                .relationType(RelationType.FRIEND)
                .source(authenticatedUser)
                .target(newFriend)
                .build();
        relationRepository.save(relation);
    }

    public void deleteFriend(UserEntity authenticatedUser, UserEntity friendToDelete) {
        RelationEntity relationToDelete = relationRepository.findBySource_IdAndTarget_IdAndRelationType(authenticatedUser.getId(), friendToDelete.getId(),
                RelationType.FRIEND);
        relationRepository.delete(relationToDelete);
    }

    public boolean isFriendAlready(UserEntity authenticatedUser, Long friendToAddId) {
        return relationRepository.findBySource_IdAndTarget_IdAndRelationType(authenticatedUser.getId(), friendToAddId, RelationType.FRIEND) != null;
    }
}

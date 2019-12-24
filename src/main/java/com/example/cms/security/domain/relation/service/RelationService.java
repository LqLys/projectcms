package com.example.cms.security.domain.relation.service;

import com.example.cms.security.domain.relation.entity.RelationEntity;
import com.example.cms.security.domain.relation.entity.RelationType;
import com.example.cms.security.domain.relation.repository.RelationRepository;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
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

    public boolean isBlockedAlready(UserEntity authenticatedUser, Long userToBlockId) {
        return relationRepository.findBySource_IdAndTarget_IdAndRelationType(authenticatedUser.getId(), userToBlockId, RelationType.BLOCKED) != null;

    }

    public List<UserEntity> getBlockedUsers(Long userId) {
        return relationRepository.getBlockedUsers(userId);
    }

    public void blockUser(UserEntity authenticatedUser, UserEntity blockUserDto) {
        RelationEntity relation = RelationEntity.builder()
                .relationType(RelationType.BLOCKED)
                .source(authenticatedUser)
                .target(blockUserDto)
                .build();
        relationRepository.save(relation);
    }

    public void unblockUser(UserEntity authenticatedUser, UserEntity userToUnblock) {

        RelationEntity relationToDelete = relationRepository.findBySource_IdAndTarget_IdAndRelationType(authenticatedUser.getId(),
                userToUnblock.getId(),
                RelationType.BLOCKED);
        relationRepository.delete(relationToDelete);
    }

    public void addGroupMembersToFriends(TravelGroupEntity travelGroup) {
        List<UserEntity> members = travelGroup.getUsers();
        members.forEach(m1 -> members.stream()
                .filter(m2 -> !m2.equals(m1))
                .filter(m2 -> !isFriendAlready(m1,m2.getId())).forEach(m2-> addFriend(m1,m2)));
    }
}

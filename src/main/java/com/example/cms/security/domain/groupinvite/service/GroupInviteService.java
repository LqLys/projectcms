package com.example.cms.security.domain.groupinvite.service;

import com.example.cms.security.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.security.domain.groupinvite.entity.GroupInviteEntity;
import com.example.cms.security.domain.groupinvite.repository.GroupInviteRepository;
import com.example.cms.security.domain.relation.service.RelationService;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteDto;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupInviteService {

    private final GroupInviteRepository groupInviteRepository;
    private final RelationService relationService;



    public GroupInviteService(GroupInviteRepository groupInviteRepository, RelationService relationService) {
        this.groupInviteRepository = groupInviteRepository;
        this.relationService = relationService;
    }


    public void sendInvitation(UserEntity authenticatedUser, UserEntity invitationTarget, TravelGroupEntity group) {
        boolean userIsBlocked = relationService.isBlockedAlready(invitationTarget, authenticatedUser.getId());
        if(!userIsBlocked) {
            GroupInviteEntity build = GroupInviteEntity.builder()
                    .invitationSource(authenticatedUser)
                    .invitationTarget(invitationTarget)
                    .travelGroup(group)
                    .status(GroupInvitationStatus.PENDING)
                    .build();

            groupInviteRepository.save(build);
        }
    }

    public List<GroupInviteDto> getUsersPendingInvites(Long userId) {
        return groupInviteRepository.getUsersInvites(userId, GroupInvitationStatus.PENDING);
    }

    public GroupInviteEntity getInvitation(Long id) {
        return groupInviteRepository.getOne(id);
    }

    public void save(GroupInviteEntity invitation) {
        groupInviteRepository.save(invitation);
    }
}

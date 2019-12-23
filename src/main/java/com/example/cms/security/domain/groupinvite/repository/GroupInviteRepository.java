package com.example.cms.security.domain.groupinvite.repository;

import com.example.cms.security.domain.groupinvite.entity.GroupInviteEntity;
import com.example.cms.security.domain.groupinvite.repository.fragment.GroupInviteRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GroupInviteRepository extends JpaRepository<GroupInviteEntity, Long>, GroupInviteRepositoryFragment {

    Optional<GroupInviteEntity> findByInvitationSource_IdAndInvitationTarget_IdAndTravelGroup_id(Long sourceId, Long targetId,
                                                                                                 Long groupId);

}

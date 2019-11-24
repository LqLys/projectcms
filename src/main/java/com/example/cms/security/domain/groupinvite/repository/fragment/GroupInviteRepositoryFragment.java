package com.example.cms.security.domain.groupinvite.repository.fragment;

import com.example.cms.security.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.security.domain.travelgroup.dto.GroupInviteDto;

import java.util.List;

public interface GroupInviteRepositoryFragment {

    List<GroupInviteDto> getUsersInvites(Long id, GroupInvitationStatus groupInvitationStatus);
}

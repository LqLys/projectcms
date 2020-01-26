package com.example.cms.app.domain.groupinvite.repository.fragment;

import com.example.cms.app.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.app.domain.travelgroup.dto.GroupInviteDto;

import java.util.List;

public interface GroupInviteRepositoryFragment {

    List<GroupInviteDto> getUsersInvites(Long id, GroupInvitationStatus groupInvitationStatus);
}

package com.example.cms.app.domain.travelgroup.repository.fragment;

import com.example.cms.app.domain.travelgroup.dto.GroupDetailsMembers;
import com.example.cms.app.domain.travelgroup.repository.UserGroupsDto;

import java.util.List;

public interface TravelGroupRepositoryFragment {

    List<UserGroupsDto> getUserGroupsByUserId(Long userId);
    List<GroupDetailsMembers> getGroupDetailsMembers(Long groupId);
}

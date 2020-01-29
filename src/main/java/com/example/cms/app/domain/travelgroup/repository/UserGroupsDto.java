package com.example.cms.app.domain.travelgroup.repository;

import com.example.cms.app.domain.usertravelgroup.entity.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupsDto {

    private Long groupId;
    private String groupName;
    private GroupRole groupRole;
}

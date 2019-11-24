package com.example.cms.security.domain.travelgroup.dto;

import com.example.cms.security.domain.usertravelgroup.entity.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailsMembers {

    private Long userId;
    private String firstName;
    private String lastName;
    private GroupRole groupRole;

}

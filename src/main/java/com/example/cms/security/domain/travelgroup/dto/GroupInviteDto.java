package com.example.cms.security.domain.travelgroup.dto;

import com.example.cms.security.domain.groupinvite.entity.GroupInvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInviteDto {

    private Long id;
    private Long invitationSourceId;
    private String invitationSourceEmail;
    private Long invitationTargetId;
    private Long groupId;
    private String groupName;
    private GroupInvitationStatus status;
}

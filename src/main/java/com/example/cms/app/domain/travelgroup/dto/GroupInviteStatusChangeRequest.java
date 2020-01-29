package com.example.cms.app.domain.travelgroup.dto;

import com.example.cms.app.domain.groupinvite.entity.GroupInvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInviteStatusChangeRequest {

    private Long id;
    private GroupInvitationStatus status;
}

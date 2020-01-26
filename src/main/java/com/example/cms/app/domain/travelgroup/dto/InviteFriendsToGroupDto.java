package com.example.cms.app.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteFriendsToGroupDto {

    private Long groupId;
    private List<Long> friendIds;
}

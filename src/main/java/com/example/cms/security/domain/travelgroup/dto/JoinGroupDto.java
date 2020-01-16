package com.example.cms.security.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupDto {

    private Long browsingUserId;
    private Long groupId;
}

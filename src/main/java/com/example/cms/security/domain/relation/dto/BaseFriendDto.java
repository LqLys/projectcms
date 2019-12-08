package com.example.cms.security.domain.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseFriendDto {

    private Long id;
    private String firstName;
    private String lastName;
}

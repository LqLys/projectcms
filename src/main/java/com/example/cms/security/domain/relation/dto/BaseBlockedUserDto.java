package com.example.cms.security.domain.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseBlockedUserDto {

    private Long id;
    private String firstName;
    private String lastName;
}

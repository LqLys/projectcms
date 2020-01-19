package com.example.cms.security.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDto {

    private String firstName;
    private String lastName;
    private String avatarUrl;
}

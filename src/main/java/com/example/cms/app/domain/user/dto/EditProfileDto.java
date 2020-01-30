package com.example.cms.app.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDto {

    @NotBlank(message = "Imię nie może być puste")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste")
    private String lastName;
    private String avatarUrl;
}

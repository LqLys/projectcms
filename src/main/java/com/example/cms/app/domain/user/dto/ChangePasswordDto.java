package com.example.cms.app.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    private String oldPassword;
    @Size(min = 3, max = 50, message = "Hasło może składać się z 3 do 50 znaków")
    private String newPassword;
}

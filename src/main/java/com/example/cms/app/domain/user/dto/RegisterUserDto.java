package com.example.cms.app.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    @NotBlank(message = "Imię jest wymagane")
    private String firstName;
    @NotBlank(message = "Nazwisko jest wymagane")
    private String lastName;
    @Email(message = "Format adresu email jest niepoprawny")
    private String email;
    @Size(min = 3, max = 50, message = "Hasło musi składać się z 3 do 50 znaków")
    private String password;
}

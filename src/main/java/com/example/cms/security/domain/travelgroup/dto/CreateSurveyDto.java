package com.example.cms.security.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSurveyDto {

    private String question;
    private List<String> answers;
}

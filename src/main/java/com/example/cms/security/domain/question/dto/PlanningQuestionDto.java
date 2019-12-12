package com.example.cms.security.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanningQuestionDto {

    private Long id;
    private String text;
    private List<PlanningAnswerDto> answers;

}

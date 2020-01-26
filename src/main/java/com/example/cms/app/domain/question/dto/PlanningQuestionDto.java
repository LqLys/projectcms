package com.example.cms.app.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanningQuestionDto {

    private Long id;
    private String text;
    private List<PlanningAnswerDto> answers;
    private Boolean isActive;

}

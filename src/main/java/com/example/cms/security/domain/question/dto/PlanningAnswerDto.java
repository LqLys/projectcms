package com.example.cms.security.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanningAnswerDto {

    private Long id;
    private String text;
    private Boolean checked;
    private Long answerCount;
}

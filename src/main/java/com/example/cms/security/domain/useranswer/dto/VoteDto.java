package com.example.cms.security.domain.useranswer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private Long userId;
    private List<Long> answerIds;
    private Long questionId;
}

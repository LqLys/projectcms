package com.example.cms.security.domain.question.facade;

import com.example.cms.security.domain.question.dto.PlanningQuestionDto;
import com.example.cms.security.domain.question.entity.QuestionEntity;
import com.example.cms.security.domain.question.service.QuestionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionFacade {

    private final QuestionService questionService;

    public QuestionFacade(QuestionService questionService) {
        this.questionService = questionService;
    }

    public List<PlanningQuestionDto> getQuestionsByGroupId(Long groupId) {
        List<QuestionEntity> questions = questionService.getQuestionsByGroupId(groupId);
        return null;
    }
}

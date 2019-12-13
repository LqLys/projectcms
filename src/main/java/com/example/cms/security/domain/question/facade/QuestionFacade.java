package com.example.cms.security.domain.question.facade;

import com.example.cms.security.domain.answer.entity.AnswerEntity;
import com.example.cms.security.domain.answer.service.AnswerService;
import com.example.cms.security.domain.question.dto.PlanningAnswerDto;
import com.example.cms.security.domain.question.dto.PlanningQuestionDto;
import com.example.cms.security.domain.question.entity.QuestionEntity;
import com.example.cms.security.domain.question.entity.QuestionStatus;
import com.example.cms.security.domain.question.service.QuestionService;
import com.example.cms.security.domain.travelgroup.dto.CreateQuestionDto;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionFacade {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final TravelGroupService travelGroupService;

    public QuestionFacade(QuestionService questionService, AnswerService answerService, TravelGroupService travelGroupService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.travelGroupService = travelGroupService;
    }

    public List<PlanningQuestionDto> getQuestionsByGroupId(Long groupId) {
        List<QuestionEntity> questions = questionService.getQuestionsByGroupId(groupId);
        return questions.stream().map(this::entityToPlanningQuestion).collect(Collectors.toList());
    }

    private PlanningQuestionDto entityToPlanningQuestion(QuestionEntity q) {
        return PlanningQuestionDto.builder()
                .id(q.getId())
                .text(q.getText())
                .answers(q.getAnswers().stream().map(this::answerToPlanningAnswer).collect(Collectors.toList()))
                .build();
    }

    private PlanningAnswerDto answerToPlanningAnswer(AnswerEntity a) {
        return PlanningAnswerDto.builder()
                .id(a.getId())
                .text(a.getText())
                .build();
    }

    public void createQuestionWithAnswers(CreateQuestionDto createQuestionDto) {
        String questionText = createQuestionDto.getText();
        TravelGroupEntity travelGroup = travelGroupService.findTravelGroup(createQuestionDto.getGroupId());
        QuestionEntity question = QuestionEntity.builder()
                .text(questionText)
                .endDate(createQuestionDto.getEndDate())
                .group(travelGroup)
                .status(QuestionStatus.OPEN)
                .build();

        questionService.createQuestion(question);
        List<AnswerEntity> answers = createQuestionDto.getAnswers().stream()
                .map(answer -> this.answerService.createAnswer(answer, question))
                .collect(Collectors.toList());
        question.setAnswers(answers);

    }
}

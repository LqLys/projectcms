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
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import com.example.cms.security.domain.useranswer.entity.UserAnswerEntity;
import com.example.cms.security.domain.useranswer.repository.UserAnswerRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuestionFacade {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final TravelGroupService travelGroupService;
    private final UserAnswerRepository userAnswerRepository;

    public QuestionFacade(QuestionService questionService, AnswerService answerService, TravelGroupService travelGroupService, UserAnswerRepository userAnswerRepository) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.travelGroupService = travelGroupService;
        this.userAnswerRepository = userAnswerRepository;
    }

    public List<PlanningQuestionDto> getQuestionsByGroupId(UserEntity authenticatedUser, Long groupId) {
        List<QuestionEntity> questions = questionService.getQuestionsByGroupId(groupId);
        final List<Long> answerIds = questions.stream().map(QuestionEntity::getAnswers)
                .flatMap(Collection::stream)
                .map(AnswerEntity::getId)
                .collect(Collectors.toList());
        final List<UserAnswerEntity> allUsersAnswers = userAnswerRepository.findAllById_AnswerIdIn(answerIds);
        final Map<Long, Long> answersCount = allUsersAnswers.stream().collect(Collectors.groupingBy(a -> a.getId().getAnswerId(), Collectors.counting()));
        final List<UserAnswerEntity> userAnswers = userAnswerRepository.findAllById_UserIdAndId_AnswerIdIn(authenticatedUser.getId(), answerIds);
        final Set<Long> userAnswerIds = userAnswers.stream()
                .map(ua -> ua.getId().getAnswerId()).collect(Collectors.toSet());
        return questions.stream().map(q-> entityToPlanningQuestion(q, userAnswerIds, answersCount)).collect(Collectors.toList());
    }

    private PlanningQuestionDto entityToPlanningQuestion(QuestionEntity q, Set<Long> userAnswerIds, Map<Long, Long> answersCount) {
        return PlanningQuestionDto.builder()
                .id(q.getId())
                .text(q.getText())
                .answers(q.getAnswers().stream().map(a-> answerToPlanningAnswer(a, userAnswerIds, answersCount))
                        .collect(Collectors.toList()))
                .build();
    }

    private PlanningAnswerDto answerToPlanningAnswer(AnswerEntity a, Set<Long> userAnswerIds, Map<Long, Long> answersCount) {
        return PlanningAnswerDto.builder()
                .id(a.getId())
                .text(a.getText())
                .checked(userAnswerIds.contains(a.getId()))
                .answerCount(answersCount.getOrDefault(a.getId(), 0L))
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

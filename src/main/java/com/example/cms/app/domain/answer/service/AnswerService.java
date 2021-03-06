package com.example.cms.app.domain.answer.service;

import com.example.cms.app.domain.answer.entity.AnswerEntity;
import com.example.cms.app.domain.answer.repository.AnswerRepository;
import com.example.cms.app.domain.question.entity.QuestionEntity;
import com.example.cms.app.domain.question.repository.QuestionRepository;
import com.example.cms.app.domain.useranswer.dto.VoteDto;
import com.example.cms.app.domain.useranswer.entity.UserAnswerEntity;
import com.example.cms.app.domain.useranswer.entity.UserAnswerId;
import com.example.cms.app.domain.useranswer.repository.UserAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, UserAnswerRepository userAnswerRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public AnswerEntity createAnswer(String text, QuestionEntity question) {
        AnswerEntity answer = AnswerEntity.builder()
                .question(question)
                .text(text)
                .build();
        return answerRepository.save(answer);
    }

    public void addVote(VoteDto voteDto) {
        final List<Long> answerIds = questionRepository.getOne(voteDto.getQuestionId()).getAnswers().stream().map(AnswerEntity::getId).collect(Collectors.toList());
        final List<UserAnswerEntity> userVotes = userAnswerRepository.findAllById_UserIdAndId_AnswerIdIn(voteDto.getUserId(), answerIds);
        userAnswerRepository.deleteAll(userVotes);

        final List<UserAnswerEntity> newAnswers = voteDto.getAnswerIds().stream()
                .map(id -> new UserAnswerEntity(new UserAnswerId(voteDto.getUserId(), id))).collect(Collectors.toList());
        userAnswerRepository.saveAll(newAnswers);
    }
}

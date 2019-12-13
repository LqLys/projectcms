package com.example.cms.security.domain.answer.service;

import com.example.cms.security.domain.answer.entity.AnswerEntity;
import com.example.cms.security.domain.answer.repository.AnswerRepository;
import com.example.cms.security.domain.question.entity.QuestionEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public AnswerEntity createAnswer(String text, QuestionEntity question) {
        AnswerEntity answer = AnswerEntity.builder()
                .question(question)
                .text(text)
                .build();
        return answerRepository.save(answer);
    }
}

package com.example.cms.security.domain.question.service;

import com.example.cms.security.domain.question.entity.QuestionEntity;
import com.example.cms.security.domain.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public List<QuestionEntity> getQuestionsByGroupId(Long groupId) {
        return null;
    }
}

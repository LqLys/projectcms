package com.example.cms.security.domain.question.repository;

import com.example.cms.security.domain.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
}

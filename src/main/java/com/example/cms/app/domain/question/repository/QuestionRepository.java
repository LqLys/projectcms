package com.example.cms.app.domain.question.repository;

import com.example.cms.app.domain.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findAllByGroup_Id(Long groupId);
}

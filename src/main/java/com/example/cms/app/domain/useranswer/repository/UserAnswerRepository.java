package com.example.cms.app.domain.useranswer.repository;

import com.example.cms.app.domain.useranswer.entity.UserAnswerEntity;
import com.example.cms.app.domain.useranswer.entity.UserAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswerEntity, UserAnswerId> {

    List<UserAnswerEntity> findAllById_UserIdAndId_AnswerIdIn(Long userId, List<Long> answerIds);

    List<UserAnswerEntity> findAllById_AnswerIdIn(List<Long> answerIds);
}

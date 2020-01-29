package com.example.cms.app.domain.answer.repository;


import com.example.cms.app.domain.answer.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

}

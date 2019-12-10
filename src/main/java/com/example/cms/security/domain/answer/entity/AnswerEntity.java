package com.example.cms.security.domain.answer.entity;

import com.example.cms.security.domain.question.entity.QuestionEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANSWER")
public class AnswerEntity {

    private static final String SEQ_ANSWER = "SEQ_ANSWER";
    @SequenceGenerator(name = SEQ_ANSWER, sequenceName = SEQ_ANSWER, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_ANSWER)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity question;
}

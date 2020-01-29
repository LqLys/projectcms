package com.example.cms.app.domain.answer.entity;

import com.example.cms.app.domain.question.entity.QuestionEntity;
import com.example.cms.app.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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

    @ManyToMany
    @JoinTable(name = "USER_ANSWER",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ANSWER_ID")})
    private List<UserEntity> users;
}

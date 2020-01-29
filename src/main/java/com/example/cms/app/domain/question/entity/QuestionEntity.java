package com.example.cms.app.domain.question.entity;


import com.example.cms.app.domain.answer.entity.AnswerEntity;
import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "QUESTION")
public class QuestionEntity {

    private static final String SEQ_QUESTION = "SEQ_QUESTION";
    @SequenceGenerator(name = SEQ_QUESTION, sequenceName = SEQ_QUESTION, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_QUESTION)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private QuestionStatus status;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerEntity> answers;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private TravelGroupEntity group;
}

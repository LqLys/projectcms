package com.example.cms.app.domain.travelgroup.entity;


import com.example.cms.app.domain.chatmessage.entity.ChatMessageEntity;
import com.example.cms.app.domain.expense.entity.ExpenseEntity;
import com.example.cms.app.domain.question.entity.QuestionEntity;
import com.example.cms.app.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "TRAVEL_GROUP")
public class TravelGroupEntity {

    private static final String SEQ_TRAVEL_GROUP = "SEQ_TRAVEL_GROUP";
    @SequenceGenerator(name = SEQ_TRAVEL_GROUP, sequenceName = SEQ_TRAVEL_GROUP, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_TRAVEL_GROUP)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "SIGN_OUT_DEADLINE")
    private LocalDate signOutDeadline;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "GROUP_VISIBILITY")
    @Enumerated(EnumType.STRING)
    private GroupVisibility groupVisibility;

    @Column(name = "GROUP_STATUS")
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;

    @Column(name = "DEBT_LIMIT")
    private BigDecimal debtLimit;

    @Column(name = "LAT")
    private BigDecimal lat;

    @Column(name = "LNG")
    private BigDecimal lng;

    @ManyToMany(mappedBy = "travelGroups")
    private List<UserEntity> users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ChatMessageEntity> chatMessages;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<QuestionEntity> questions;

    @Column(name = "IMG_URL")
    private String imgUrl;


}

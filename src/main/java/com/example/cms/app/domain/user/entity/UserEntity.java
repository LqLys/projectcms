package com.example.cms.app.domain.user.entity;

import com.example.cms.app.domain.answer.entity.AnswerEntity;
import com.example.cms.app.domain.chatmessage.entity.ChatMessageEntity;
import com.example.cms.app.domain.expense.entity.ExpenseEntity;
import com.example.cms.app.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import com.example.cms.app.domain.groupinvite.entity.GroupInviteEntity;
import com.example.cms.app.domain.relation.entity.RelationEntity;
import com.example.cms.app.domain.role.entity.RoleEntity;
import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERS")
public class UserEntity {

    private static final String SEQ_USER = "SEQ_USER";
    @SequenceGenerator(name = SEQ_USER, sequenceName = SEQ_USER, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_USER)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    @Email(message = "Niepoprawny adres email")
    private String email;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @ManyToMany
    @JoinTable(name = "USER_ROLE",
    joinColumns = {@JoinColumn(name = "USER_ID")},
    inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private List<RoleEntity> roles;

    @ManyToMany
    @JoinTable(name = "USER_TRAVEL_GROUP",
    joinColumns = {@JoinColumn(name = "USER_ID")},
    inverseJoinColumns = {@JoinColumn(name = "TRAVEL_GROUP_ID")})
    private List<TravelGroupEntity> travelGroups;

    @OneToMany(mappedBy = "invitationSource", cascade = CascadeType.ALL)
    private List<GroupInviteEntity> sendInvitations;

    @OneToMany(mappedBy = "invitationTarget", cascade = CascadeType.ALL)
    private List<GroupInviteEntity> receivedInvitations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatMessageEntity> chatMessages;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL)
    private List<ExpenseParticipantEntity> lended;

    @OneToMany(mappedBy = "debtor", cascade = CascadeType.ALL)
    private List<ExpenseParticipantEntity> debts;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
    private List<RelationEntity> relationsToMe;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    private List<RelationEntity> relationsToOthers;

    @ManyToMany(mappedBy = "users")
    private List<AnswerEntity> answers;






}

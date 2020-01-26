package com.example.cms.app.domain.groupinvite.entity;


import com.example.cms.app.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.app.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "GROUP_INVITE")
public class GroupInviteEntity {

    private static final String SEQ_GROUP_INVITE = "SEQ_GROUP_INVITE";
    @SequenceGenerator(name = SEQ_GROUP_INVITE, sequenceName = SEQ_GROUP_INVITE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_GROUP_INVITE)
    @Column(name = "ID")
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private GroupInvitationStatus status;

    @ManyToOne
    @JoinColumn(name = "INVITATION_SOURCE_ID")
    private UserEntity invitationSource;

    @ManyToOne
    @JoinColumn(name = "INVITATION_TARGET_ID")
    private UserEntity invitationTarget;

    @ManyToOne
    @JoinColumn(name = "TRAVEL_GROUP_ID")
    private TravelGroupEntity travelGroup;



}

package com.example.cms.security.domain.usertravelgroup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_TRAVEL_GROUP")
public class UserTravelGroupEntity {

    @EmbeddedId
    private UserTravelGroupId id;

    @Column(name = "GROUP_ROLE")
    @Enumerated(EnumType.STRING)
    private GroupRole groupRole;
}

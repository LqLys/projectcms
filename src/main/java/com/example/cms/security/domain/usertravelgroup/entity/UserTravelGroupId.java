package com.example.cms.security.domain.usertravelgroup.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserTravelGroupId implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "travel_group_id")
    private Long travelGroupId;
}

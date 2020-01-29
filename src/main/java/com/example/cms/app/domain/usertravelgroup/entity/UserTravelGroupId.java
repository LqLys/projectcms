package com.example.cms.app.domain.usertravelgroup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTravelGroupId implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "travel_group_id")
    private Long travelGroupId;
}

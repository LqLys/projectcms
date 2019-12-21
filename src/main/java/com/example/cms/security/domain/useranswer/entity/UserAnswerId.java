package com.example.cms.security.domain.useranswer.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerId implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "answer_id")
    private Long answerId;
}

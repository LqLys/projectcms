package com.example.cms.app.domain.useranswer.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_ANSWER")
public class UserAnswerEntity {

    @EmbeddedId
    private UserAnswerId id;
}

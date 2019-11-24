package com.example.cms.security.domain.chatmessage.entity;


import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CHAT_MESSAGE")
@Builder
public class ChatMessageEntity {

    private static final String SEQ_CHAT_MESSAGE = "SEQ_CHAT_MESSAGE";
    @SequenceGenerator(name = SEQ_CHAT_MESSAGE, sequenceName = SEQ_CHAT_MESSAGE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_CHAT_MESSAGE)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "TIME")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private TravelGroupEntity group;
}

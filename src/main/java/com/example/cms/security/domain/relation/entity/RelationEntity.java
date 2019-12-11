package com.example.cms.security.domain.relation.entity;

import com.example.cms.security.domain.user.entity.UserEntity;
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
@Table(name = "RELATION")
public class RelationEntity {

    private static final String SEQ_RELATION = "SEQ_RELATION";
    @SequenceGenerator(name = SEQ_RELATION, sequenceName = SEQ_RELATION, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_RELATION)
    @Column(name = "ID")
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private RelationType relationType;

    @ManyToOne
    @JoinColumn(name = "SOURCE_USER_ID")
    private UserEntity source;

    @ManyToOne
    @JoinColumn(name = "TARGET_USER_ID")
    private UserEntity target;
}

package com.example.cms.security.domain.user.entity;

import com.example.cms.security.domain.role.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
    private String email;

    @Column(name = "ACTIVE")
    private Boolean active;

    @ManyToMany
    @JoinTable(name = "USER_ROLE",
    joinColumns = {@JoinColumn(name = "USER_ID")},
    inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private List<RoleEntity> roles;






}

package com.example.cms.security.domain.user.repository;

import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity getById(Long id);

    List<UserEntity> findAllByIdIn(List<Long> debtors);
}

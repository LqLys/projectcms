package com.example.cms.app.domain.role.repository;

import com.example.cms.app.domain.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(String role);

}

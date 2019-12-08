package com.example.cms.security.domain.relation.repository;

import com.example.cms.security.domain.relation.entity.RelationEntity;
import com.example.cms.security.domain.relation.repository.fragment.RelationRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<RelationEntity, Long>, RelationRepositoryFragment {
}

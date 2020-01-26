package com.example.cms.app.domain.relation.repository;

import com.example.cms.app.domain.relation.entity.RelationEntity;
import com.example.cms.app.domain.relation.entity.RelationType;
import com.example.cms.app.domain.relation.repository.fragment.RelationRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<RelationEntity, Long>, RelationRepositoryFragment {

    RelationEntity findBySource_IdAndTarget_IdAndRelationType(Long sourceId, Long targetId, RelationType relationType);
}

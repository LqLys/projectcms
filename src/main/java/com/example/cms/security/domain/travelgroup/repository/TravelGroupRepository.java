package com.example.cms.security.domain.travelgroup.repository;

import com.example.cms.security.domain.travelgroup.entity.GroupStatus;
import com.example.cms.security.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.repository.fragment.TravelGroupRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelGroupRepository extends JpaRepository<TravelGroupEntity, Long>, TravelGroupRepositoryFragment {

    List<TravelGroupEntity> findAllById(Long id);
    List<TravelGroupEntity> findAllByGroupStatusAndGroupVisibility(GroupStatus groupStatus, GroupVisibility groupVisibility);

}

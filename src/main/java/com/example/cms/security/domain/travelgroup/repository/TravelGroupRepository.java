package com.example.cms.security.domain.travelgroup.repository;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelGroupRepository extends JpaRepository<TravelGroupEntity, Long> {

    List<TravelGroupEntity> findAllById(Long id);

}

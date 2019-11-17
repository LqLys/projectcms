package com.example.cms.security.domain.travelgroup.service;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.travelgroup.repository.TravelGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class TravelGroupService {
    
    private final TravelGroupRepository travelGroupRepository;

    public TravelGroupService(TravelGroupRepository travelGroupRepository) {
        this.travelGroupRepository = travelGroupRepository;
    }


    public List<TravelGroupEntity> getTravelGroups(Long id) {
        return travelGroupRepository.findAllById(id);
    }

    public void createTravelGroup(TravelGroupEntity travelGroup) {
        travelGroupRepository.save(travelGroup);
    }
}

package com.example.cms.security.domain.usertravelgroup.service;

import com.example.cms.security.domain.usertravelgroup.repository.UserTravelGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class UserTravelGroupService {


    private final UserTravelGroupRepository userTravelGroupRepository;

    public UserTravelGroupService(UserTravelGroupRepository userTravelGroupRepository) {
        this.userTravelGroupRepository = userTravelGroupRepository;
    }


}

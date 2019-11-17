package com.example.cms.security.domain.usertravelgroup.facade;

import com.example.cms.security.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

@Component
public class UserTravelGroupFacade {

    private final UserTravelGroupService userTravelGroupService;

    public UserTravelGroupFacade(UserTravelGroupService userTravelGroupService) {
        this.userTravelGroupService = userTravelGroupService;
    }
}

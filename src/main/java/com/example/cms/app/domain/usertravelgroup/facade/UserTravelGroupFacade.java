package com.example.cms.app.domain.usertravelgroup.facade;

import com.example.cms.app.domain.usertravelgroup.service.UserTravelGroupService;
import org.springframework.stereotype.Component;

@Component
public class UserTravelGroupFacade {

    private final UserTravelGroupService userTravelGroupService;

    public UserTravelGroupFacade(UserTravelGroupService userTravelGroupService) {
        this.userTravelGroupService = userTravelGroupService;
    }
}

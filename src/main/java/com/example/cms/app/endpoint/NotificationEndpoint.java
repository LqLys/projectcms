package com.example.cms.app.endpoint;

import com.example.cms.app.domain.groupinvite.facade.GroupInviteFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/notification")
public class NotificationEndpoint {

    private final GroupInviteFacade groupInviteFacade;

    public NotificationEndpoint(GroupInviteFacade groupInviteFacade) {
        this.groupInviteFacade = groupInviteFacade;
    }

    @GetMapping("/pending/{username}")
    public Long getNumberOfPendingInvitations(@PathVariable("username") String username){
        return groupInviteFacade.getNumberOfPendingInvitations(username);
    }
}

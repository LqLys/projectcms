package com.example.cms.app.domain.travelgroup.controller;

import com.example.cms.app.domain.expense.facade.ExpenseFacade;
import com.example.cms.app.domain.groupinvite.entity.GroupInvitationStatus;
import com.example.cms.app.domain.groupinvite.facade.GroupInviteFacade;
import com.example.cms.app.domain.question.dto.PlanningQuestionDto;
import com.example.cms.app.domain.question.facade.QuestionFacade;
import com.example.cms.app.domain.relation.dto.BaseFriendDto;
import com.example.cms.app.domain.relation.facade.RelationFacade;
import com.example.cms.app.domain.travelgroup.dto.*;
import com.example.cms.app.domain.travelgroup.entity.GroupStatus;
import com.example.cms.app.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.app.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.app.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import com.example.cms.app.domain.usertravelgroup.entity.GroupRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/group")
public class TravelGroupController {

    @Value("${api.base-url}")
    private String API_BASE;

    @Value("${google.maps-key}")
    private String MAPS_KEY;

    private final TravelGroupFacade travelGroupFacade;
    private final GroupInviteFacade groupInviteFacade;
    private final UserService userService;
    private final ExpenseFacade expenseFacade;
    private final RelationFacade relationFacade;
    private final QuestionFacade questionFacade;

    public TravelGroupController(TravelGroupFacade travelGroupFacade,
                                 GroupInviteFacade groupInviteFacade,
                                 UserService userService, ExpenseFacade expenseFacade, RelationFacade relationFacade, QuestionFacade questionFacade) {
        this.travelGroupFacade = travelGroupFacade;
        this.groupInviteFacade = groupInviteFacade;
        this.userService = userService;
        this.expenseFacade = expenseFacade;
        this.relationFacade = relationFacade;
        this.questionFacade = questionFacade;
    }

    @GetMapping(path = "/groups")
    public ModelAndView getGroups(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByEmail(auth.getName());
        List<BaseFriendDto> userFriends = relationFacade.getUserFriends(user.getId());
        List<UserGroupsDto> travelGroups = travelGroupFacade.getUserGroupRoles(user.getId());
        List<GroupInviteDto> pendingInvites = groupInviteFacade.getUsersPendingInvites(user.getId());
        modelAndView.addObject("userFriends", userFriends);
        modelAndView.addObject("newTravelGroup", new CreateTravelGroupRequest());
        modelAndView.addObject("pendingInvites", pendingInvites);
        modelAndView.addObject("statusChangeRequest", new GroupInviteStatusChangeRequest());
        modelAndView.addObject("travelGroups", travelGroups);
        modelAndView.setViewName("group/groups");
        return modelAndView;
    }

    @PostMapping(path = "/group-add")
    public ModelAndView createGroup(@Valid CreateTravelGroupRequest createTravelGroupRequest,
                                    BindingResult bindingResult, ModelAndView modelAndView){
        UserEntity user = userService.getAuthenticatedUser();
        travelGroupFacade.createTravelGroup(createTravelGroupRequest, user);
        modelAndView.setViewName("redirect:/group/groups");
        return modelAndView;
    }

    @GetMapping(path = "/details/{groupId}/members")
    public ModelAndView getGroupDetailMembers(@PathVariable("groupId") Long groupId, ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<GroupDetailsMembers> groupDetailsMembers = travelGroupFacade.getGroupDetailsMembers(groupId);
        List<BaseFriendDto> friends = relationFacade.getUserFriends(authenticatedUser.getId()).stream()
                .filter(f-> travelGroupFacade.userAlreadyInGroup(f.getId(), groupId))
                .collect(Collectors.toList());
        TravelGroupDto travelGroup = travelGroupFacade.getTravelGroupById(groupId);
        GroupRole browsingUserRole = groupDetailsMembers.stream()
                .filter(user -> authenticatedUser.getId().equals(user.getUserId()))
                .findFirst()
                .map(GroupDetailsMembers::getGroupRole)
                .orElse(GroupRole.MEMBER);
        boolean browsingIsMember = groupDetailsMembers.stream()
                .map(GroupDetailsMembers::getUserId)
                .anyMatch(id -> id.equals(authenticatedUser.getId()));
        modelAndView.addObject("browsingUserId", authenticatedUser.getId());
        modelAndView.addObject("browsingUserRole", browsingUserRole);
        modelAndView.addObject("browsingIsMember", browsingIsMember);
        modelAndView.addObject("friends", friends);
        modelAndView.addObject("groupDetailsMembers", groupDetailsMembers);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("groupStatus", travelGroup.getGroupStatus());
        modelAndView.addObject("groupVisibility", travelGroup.getGroupVisibility());
        modelAndView.addObject("groupName", travelGroup.getName());
        modelAndView.addObject("groupInvitation", new GroupInviteRequest());
        modelAndView.addObject("joinGroupDto", new JoinGroupDto());
        modelAndView.setViewName("group/groupDetailsMembers");
        return modelAndView;

    }

    @PostMapping(path = "/{groupId}/join")
    public ModelAndView joinGroup(@Valid JoinGroupDto joinGroupDto,  ModelAndView modelAndView){
        modelAndView.setViewName("redirect:/group/details/" + joinGroupDto.getGroupId() + "/members");
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();
        travelGroupFacade.joinGroup(authenticatedUser, joinGroupDto.getGroupId());
        return modelAndView;

    }

    @GetMapping(path = "/details/{groupId}/expenses")
    public ModelAndView getGroupDetailExpenses(@PathVariable("groupId") Long groupId, ModelAndView modelAndView) {
        modelAndView.setViewName("group/groupDetailsExpenses");
        Long currentUserId = userService.getAuthenticatedUser().getId();
        TravelGroupExpensesDto travelGroup = travelGroupFacade.getTravelGroupExpensesView(groupId);
        modelAndView.addObject("travelGroup", travelGroup);
        modelAndView.addObject("createExpenseRequest", new CreateExpenseRequest());
        modelAndView.addObject("currentUserId", currentUserId);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("apiBase", API_BASE);

        return modelAndView;
    }

    @PostMapping(path = "/invite")
    public ModelAndView sendInvitation(@Valid GroupInviteRequest groupInviteRequest, BindingResult bindingResult,
                                       ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        travelGroupFacade.sendInvitation(authenticatedUser, groupInviteRequest);
        modelAndView.setViewName("redirect:/group/details/"+ groupInviteRequest.getGroupId()+"/members");
        return modelAndView;
    }

    @PostMapping(path = "/inviteFriends")
    public ModelAndView inviteFriends(InviteFriendsToGroupDto inviteFriendsToGroupDto, ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/group/details/"+inviteFriendsToGroupDto.getGroupId()+"/members");
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        travelGroupFacade.inviteMultipleFriends(authenticatedUser, inviteFriendsToGroupDto);

        return modelAndView;

    }

    @PostMapping(path = "/uninvite")
    public ModelAndView uninvite(@Valid GroupUninviteRequest groupUninviteRequest, BindingResult bindingResult,
                                       ModelAndView modelAndView) {
        travelGroupFacade.uninviteUser(groupUninviteRequest);
        modelAndView.setViewName("redirect:/group/details/"+groupUninviteRequest.getGroupId()+"/members");
        return modelAndView;
    }

    @PostMapping(path = "/expense")
    public ModelAndView createExpense(@Valid CreateExpenseRequest createExpenseRequest, BindingResult bindingResult,
                                       ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        expenseFacade.createExpense(createExpenseRequest, authenticatedUser);
        modelAndView.setViewName("redirect:/group/groups");
        return modelAndView;
    }

    @PostMapping(path = "/invite/status")
    public ModelAndView changeInvitationStatus(@Valid GroupInviteStatusChangeRequest groupInviteStatusChangeRequest,
                                               BindingResult bindingResult, ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();

        Long groupId = groupInviteFacade.handleInvitation(authenticatedUser, groupInviteStatusChangeRequest);
        if(groupInviteStatusChangeRequest.getInvitationStatus() == GroupInvitationStatus.ACCEPTED) {
            modelAndView.setViewName("redirect:/group/details/"+groupId+"/members");
        } else {
            modelAndView.setViewName("redirect:/notification/invitations");
        }

        return modelAndView;
    }


    @GetMapping(path = "/details/{groupId}")
    public ModelAndView getGroupDetails(@PathVariable("groupId")Long groupId,  ModelAndView modelAndView) {
        modelAndView.setViewName("group/groupDetails");
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        TravelGroupDetailsDto travelGroupDto = travelGroupFacade.getTravelGroupDetails(groupId);
        boolean viewerIsOrganizer = travelGroupFacade.viewerIsOrganizer(authenticatedUser, groupId);
        modelAndView.addObject("groupVisibilityOptions", GroupVisibility.values());
        modelAndView.addObject("groupStatusOptions", GroupStatus.values());
        modelAndView.addObject("travelGroupDto", travelGroupDto);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("viewerIsOrganizer", viewerIsOrganizer);
        modelAndView.addObject("MAPS_KEY", MAPS_KEY);
        return modelAndView;
    }
    @PostMapping(path = "/details/edit")
    public ModelAndView editGroupDetails(TravelGroupDetailsDto travelGroupDetailsDto,  ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/group/details/"+travelGroupDetailsDto.getGroupId());
        travelGroupFacade.editGroupDetails(travelGroupDetailsDto);
        return modelAndView;
    }

    @GetMapping(path = "/details/{groupId}/planning")
    public ModelAndView getGroupDetailPlanning(@PathVariable("groupId") Long groupId, ModelAndView modelAndView) {
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<PlanningQuestionDto> questions = questionFacade.getQuestionsByGroupId(authenticatedUser, groupId);
        String groupName = travelGroupFacade.getTravelGroupById(groupId).getName();

        modelAndView.addObject("questions", questions);
        modelAndView.addObject("createSurveyDto", new CreateQuestionDto());
        modelAndView.addObject("userId", authenticatedUser.getId());
        modelAndView.addObject("groupName", groupName);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("apiBase", API_BASE);
        modelAndView.setViewName("group/groupDetailsPlanning");

        return modelAndView;
    }





}

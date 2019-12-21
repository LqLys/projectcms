package com.example.cms.security.domain.travelgroup.controller;

import com.example.cms.security.domain.expense.facade.ExpenseFacade;
import com.example.cms.security.domain.groupinvite.facade.GroupInviteFacade;
import com.example.cms.security.domain.question.dto.PlanningQuestionDto;
import com.example.cms.security.domain.question.facade.QuestionFacade;
import com.example.cms.security.domain.relation.dto.BaseFriendDto;
import com.example.cms.security.domain.relation.facade.RelationFacade;
import com.example.cms.security.domain.travelgroup.dto.*;
import com.example.cms.security.domain.travelgroup.entity.GroupStatus;
import com.example.cms.security.domain.travelgroup.entity.GroupVisibility;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.travelgroup.repository.UserGroupsDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
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

@Controller
@RequestMapping(path = "/group")
public class TravelGroupController {

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

    @PostMapping(path = "")
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
        List<BaseFriendDto> friends = relationFacade.getUserFriends(authenticatedUser.getId());
        TravelGroupDto travelGroup = travelGroupFacade.getTravelGroupById(groupId);
        modelAndView.addObject("friends", friends);
        modelAndView.addObject("groupDetailsMembers", groupDetailsMembers);
        modelAndView.addObject("groupId", groupId);
        modelAndView.addObject("groupName", travelGroup.getName());
        modelAndView.addObject("groupInvitation", new GroupInviteRequest());
        modelAndView.setViewName("group/groupDetailsMembers");
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

        return modelAndView;
    }

    @PostMapping(path = "/invite")
    public ModelAndView sendInvitation(@Valid GroupInviteRequest groupInviteRequest, BindingResult bindingResult,
                                       ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        travelGroupFacade.sendInvitation(authenticatedUser, groupInviteRequest);
        modelAndView.setViewName("redirect:/group/groups");
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
        Long groupId = groupInviteFacade.acceptInvitation(authenticatedUser, groupInviteStatusChangeRequest);
        modelAndView.setViewName("redirect:/group/details/"+groupId+"/members");
        return modelAndView;
    }

    @GetMapping(path = "/details/{groupId}")
    public ModelAndView getGroupDetails(@PathVariable("groupId")Long groupId,  ModelAndView modelAndView) {
        modelAndView.setViewName("group/groupDetails");
        TravelGroupDetailsDto travelGroupDto = travelGroupFacade.getTravelGroupDetails(groupId);
        modelAndView.addObject("groupVisibilityOptions", GroupVisibility.values());
        modelAndView.addObject("groupStatusOptions", GroupStatus.values());
        modelAndView.addObject("travelGroupDto", travelGroupDto);
        return modelAndView;
    }
    @PostMapping(path = "/details/edit")
    public ModelAndView editGroupDetails(TravelGroupDetailsDto travelGroupDetailsDto,  ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/group/details/"+travelGroupDetailsDto.getGroupId());
        travelGroupFacade.editGroup(travelGroupDetailsDto);
        return modelAndView;
    }

    @GetMapping(path = "/details/{groupId}/planning")
    public ModelAndView getGroupDetailPlanning(@PathVariable("groupId") Long groupId, ModelAndView modelAndView) {
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<PlanningQuestionDto> questions = questionFacade.getQuestionsByGroupId(authenticatedUser, groupId);

        modelAndView.addObject("questions", questions);
        modelAndView.addObject("createSurveyDto", new CreateQuestionDto());
        modelAndView.addObject("userId", authenticatedUser.getId());
        modelAndView.setViewName("group/groupDetailsPlanning");

        return modelAndView;
    }





}

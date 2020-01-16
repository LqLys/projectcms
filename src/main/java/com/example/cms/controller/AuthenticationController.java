package com.example.cms.controller;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.user.dto.ChangePasswordDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final TravelGroupFacade travelGroupFacade;

    public AuthenticationController(UserService userService, BCryptPasswordEncoder encoder, TravelGroupFacade travelGroupFacade) {
        this.userService = userService;
        this.encoder = encoder;
        this.travelGroupFacade = travelGroupFacade;
    }

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(path="/", method= RequestMethod.GET)
    public ModelAndView homepage(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        List<TravelGroupDto> availablePublicGroups = travelGroupFacade.getAllAvailableTravelGroups();
        modelAndView.addObject("availableTravelGroups", availablePublicGroups);

        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(ModelAndView modelAndView){
        UserEntity userEntity = new UserEntity();
        modelAndView.addObject("userEntity", userEntity);

        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid UserEntity userEntity, BindingResult bindingResult, ModelAndView modelAndView) {
        UserEntity userExists = userService.findUserByEmail(userEntity.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.userEntity",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(userEntity);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("userEntity", new UserEntity());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome  (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping(value = "/profile")
    public ModelAndView getProfile(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("profile");
        return modelAndView;

    }

    @GetMapping(value = "/change-password")
    public ModelAndView changePassword(ModelAndView modelAndView){
        modelAndView.addObject("changePasswordDto", new ChangePasswordDto());
        modelAndView.setViewName("change-password");
        return modelAndView;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid ChangePasswordDto changePasswordDto, BindingResult bindingResult, ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findUserByEmail(auth.getName());
        if (!encoder.matches(changePasswordDto.getOldPassword(), userEntity.getPassword())) {
            bindingResult.rejectValue("oldPassword", "error.changePasswordDto",
                    "Wrong old password");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("change-password");
        } else {
            userEntity.setPassword(changePasswordDto.getNewPassword());
            userService.saveUser(userEntity);
            modelAndView.addObject("successMessage", "Password Changed");
            modelAndView.addObject("changePasswordDto", new ChangePasswordDto());
            modelAndView.setViewName("change-password");

        }
        return modelAndView;
    }


}

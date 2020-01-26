package com.example.cms.controller;
import com.example.cms.security.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.security.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.security.domain.user.dto.ChangePasswordDto;
import com.example.cms.security.domain.user.dto.EditProfileDto;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(path = "/setContext")
    public ModelAndView setContext(ModelAndView modelAndView, HttpServletRequest request){
        modelAndView.setViewName("redirect:/");
        final String avatarUrl = userService.getAuthenticatedUser().getAvatarUrl();
        request.getSession().setAttribute("AVATAR_URL", avatarUrl);
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
                            "Użytkownik o takim adresie email został już zarejestrowany");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(userEntity);
            modelAndView.addObject("successMessage", "Użytkownik pomyślnie zarejestrowany");
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
        modelAndView.addObject("adminMessage","Odmowa dostępu: wymagane uprawnienia administratora");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping(value = "/profile")
    public ModelAndView getProfile(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findUserByEmail(auth.getName());
        final EditProfileDto editProfileDto = userEntityToProfileDto(userEntity);
        modelAndView.addObject("editProfileDto", editProfileDto);
        modelAndView.addObject("successMessage", "");
        modelAndView.setViewName("profile");
        return modelAndView;

    }

    private EditProfileDto userEntityToProfileDto(UserEntity userEntity) {
        return EditProfileDto.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .avatarUrl(userEntity.getAvatarUrl())
                .build();
    }

    @PostMapping(value = "/edit-profile")
    public ModelAndView editProfile(@Valid EditProfileDto editProfileDto, BindingResult bindingResult, ModelAndView modelAndView, HttpServletRequest request) {
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profile");
            final EditProfileDto editProfileDto1 = userEntityToProfileDto(authenticatedUser);
            modelAndView.addObject("editProfileDto", editProfileDto1);
        } else {
            request.getSession().setAttribute("AVATAR_URL", editProfileDto.getAvatarUrl());
            userService.editUser(authenticatedUser, editProfileDto);
            modelAndView.addObject("successMessage", "Dane pomyślnie zapisane");

            modelAndView.setViewName("profile");

        }
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
                    "Błędne stare hasło");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("change-password");
        } else {
            userEntity.setPassword(changePasswordDto.getNewPassword());
            userService.saveUser(userEntity);
            modelAndView.addObject("successMessage", "Hasło zmienione na nowe");
            final UserEntity authenticatedUser = userService.getAuthenticatedUser();
            final EditProfileDto editProfileDto = userEntityToProfileDto(authenticatedUser);
            modelAndView.addObject("editProfileDto", editProfileDto);
            modelAndView.setViewName("profile");
        }
        return modelAndView;
    }


}

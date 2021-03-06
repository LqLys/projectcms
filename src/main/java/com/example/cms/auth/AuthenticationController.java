package com.example.cms.auth;
import com.example.cms.app.domain.travelgroup.dto.TravelGroupDto;
import com.example.cms.app.domain.travelgroup.facade.TravelGroupFacade;
import com.example.cms.app.domain.user.dto.ChangePasswordDto;
import com.example.cms.app.domain.user.dto.EditProfileDto;
import com.example.cms.app.domain.user.dto.RegisterUserDto;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        modelAndView.addObject("registerUserDto", new RegisterUserDto());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid RegisterUserDto registerUserDto, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        UserEntity userExists = userService.findUserByEmail(registerUserDto.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.userEntity",
                            "Użytkownik o takim adresie email został już zarejestrowany");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            final UserEntity newUser = UserEntity.builder()
                    .firstName(registerUserDto.getFirstName())
                    .lastName(registerUserDto.getLastName())
                    .email(registerUserDto.getEmail())
                    .password(registerUserDto.getPassword())
                    .build();
            userService.saveUser(newUser);
            modelAndView.setViewName("redirect:/login");
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik zarejestrowany pomyślnie, możesz się zalogować.");
            redirectAttributes.addFlashAttribute("registered", true);
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

    @PostMapping(value = "/profile")
    public ModelAndView editProfile(@Valid EditProfileDto editProfileDto, BindingResult bindingResult, ModelAndView modelAndView, HttpServletRequest request) {
        final UserEntity authenticatedUser = userService.getAuthenticatedUser();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("profile");

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

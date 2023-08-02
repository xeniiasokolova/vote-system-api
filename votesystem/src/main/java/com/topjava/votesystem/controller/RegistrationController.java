package com.topjava.votesystem.controller;

import com.topjava.votesystem.util.handlers.UserHandler;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class RegistrationController {

    //@Autowired
    private final UserService userService;
    public final String REGISTRATION_TEMPLATE = "registration";

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("newUser", new User());

        return REGISTRATION_TEMPLATE;
    }

    @PostMapping
    public String addUser(@ModelAttribute("newUser") @Valid User newUser, Model model) {

        if (userService.isExistsUserByUsername(newUser)) {
            UserHandler.usernameError(model);
            return REGISTRATION_TEMPLATE;
        }

        if (userService.isExistsUserByEmail(newUser)) {
            UserHandler.emailError(model);
            return REGISTRATION_TEMPLATE;
        }

        if (newUser.getPassword().length() < 5) {
            UserHandler.passwordLengthError(model);
            return REGISTRATION_TEMPLATE;
        }

        if (!newUser.getPassword().equals(newUser.getPasswordConfirm())) {
            UserHandler.passwordMatchError(model);
            return REGISTRATION_TEMPLATE;
        }

        if (!userService.signUpUser(newUser)) {
            UserHandler.emailInvalidError(model);
            return REGISTRATION_TEMPLATE;
        }

        return "redirect:/login";
    }

}

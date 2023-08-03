package com.topjava.votesystem.controller;

import com.topjava.votesystem.util.handlers.UserHandler;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class RegistrationController {

    //@Autowired
    private final UserService userService;
    private final String REGISTRATION_TEMPLATE = "registration";

    @GetMapping
    public ModelAndView registration(Model model) {
        model.addAttribute("newUser", new User());

        return new ModelAndView(REGISTRATION_TEMPLATE);
    }

    @PostMapping
    public ModelAndView addUser(@ModelAttribute("newUser") @Valid User newUser, Model model) {

        if (userService.isExistsUserByUsername(newUser)) {
            UserHandler.usernameError(model);
            return new ModelAndView(REGISTRATION_TEMPLATE);
        }

        if (userService.isExistsUserByEmail(newUser)) {
            UserHandler.emailError(model);
            return new ModelAndView(REGISTRATION_TEMPLATE);
        }

        if (newUser.getPassword().length() < 5) {
            UserHandler.passwordLengthError(model);
            return new ModelAndView(REGISTRATION_TEMPLATE);
        }

        if (!newUser.getPassword().equals(newUser.getPasswordConfirm())) {
            UserHandler.passwordMatchError(model);
            return new ModelAndView(REGISTRATION_TEMPLATE);
        }

        if (!userService.signUpUser(newUser)) {
            UserHandler.emailInvalidError(model);
            return new ModelAndView(REGISTRATION_TEMPLATE);
        }

        return new ModelAndView("redirect:/login");
    }

}

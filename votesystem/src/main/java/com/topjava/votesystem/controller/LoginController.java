package com.topjava.votesystem.controller;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/login")
public class LoginController {
    private final UserService userService;

    @GetMapping
    public ModelAndView loginForm(Model model) {
        model.addAttribute("newUser", new User());
        return new ModelAndView("login");
    }

    @PostMapping
    public ModelAndView login(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, Model model) {

        if (!userService.isExistsUserByUsername(user)) {
            model.addAttribute("userError", "error.user.invalid");
            return new ModelAndView("login");
        }

        UserDetails storedUser = userService.loadUserByUsername(user.getUsername());
        if (!userService.isPasswordsEqual(user.getPassword(), storedUser.getPassword())) {
            model.addAttribute("passwordError", "error.password.invalid");
            return new ModelAndView("login");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", "account.error");
            return new ModelAndView("login");
        }


        return new ModelAndView("redirect:/restaurants");
    }
}


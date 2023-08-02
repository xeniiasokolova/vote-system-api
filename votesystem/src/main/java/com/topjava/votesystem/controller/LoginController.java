package com.topjava.votesystem.controller;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/login")
public class LoginController {
    private final UserService userService;

    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("newUser", new User());
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult, Model model) {

        if (!userService.isExistsUserByUsername(user)) {
            model.addAttribute("userError", "error.user.invalid");
            return "login";
        }

        UserDetails storedUser = userService.loadUserByUsername(user.getUsername());
        if (!userService.isPasswordsEqual(user.getPassword(), storedUser.getPassword())) {
            model.addAttribute("passwordError", "error.password.invalid");
            return "login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", "account.error");
            return "login";
        }


        return "redirect:/restaurants";
    }
}


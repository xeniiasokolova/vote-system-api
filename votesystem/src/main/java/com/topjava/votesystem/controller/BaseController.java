package com.topjava.votesystem.controller;

import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;


public class BaseController {

    @Autowired
    private UserService userService;

    @ModelAttribute("userRole")
    public String getUserRole(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userService.getRoleByUsername(username);
        }
        return null;
    }

    @ModelAttribute("username")
    public String getUsername(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @ModelAttribute("user")
    public User getAuthUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return (User) userService.loadUserByUsername(username);
        }
        return null;
    }
}
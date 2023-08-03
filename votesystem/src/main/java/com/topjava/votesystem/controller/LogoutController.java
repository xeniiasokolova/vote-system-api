package com.topjava.votesystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LogoutController {

    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/login");
    }
}

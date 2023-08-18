package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = ProfileUIController.REST_URL)
public class ProfileUIController extends UserAbstractController {
    static final String REST_URL = "/account";

    @GetMapping
    public String showProfilePage(Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
        return "account";
    }
}

package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = UserUIController.REST_URL)
public class UserUIController extends UserAbstractController {
    static final String REST_URL = "/users";
    static final String ADD_USER_FORM = "/users/add-user";
    static final String UPDATE_USER_FORM = "/users/update-user";

    @GetMapping
    public String showUsersPage(Model model) {
        model.addAttribute("users", super.getAll());
        return "users/users";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        return ADD_USER_FORM;
    }

    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable Long id, Model model) {
        User user = super.get(id);
        model.addAttribute("updateUser", user);
        model.addAttribute("roles", Role.values());
        return UPDATE_USER_FORM;
    }
}

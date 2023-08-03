package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.util.handlers.UserHandler;
import com.topjava.votesystem.model.Role;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UsersController extends BaseController {
    public static final String TEMPLATE_USER_ACCOUNT = "users";
    public static final String TEMPLATE_ADMIN_USERS = "admin/users";
    public static final String TEMPLATE_ADMIN_USERS_FORM = "admin/userForm";

    private final UserService userService;


    @GetMapping
    public ModelAndView readAll(Model model) {
        model.addAttribute("users", userService.readAll());
        return new ModelAndView(TEMPLATE_ADMIN_USERS);
    }


    @GetMapping("/add")
    public ModelAndView create(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return new ModelAndView(TEMPLATE_ADMIN_USERS_FORM);
    }

    @PostMapping("/add")
    public ModelAndView saveNewUser(@ModelAttribute("user") @Valid User user, Model model) {
        if (userService.isExistsUserByUsername(user)) {
            return new ModelAndView(handleUsernameExistsError(model, user));
        }
        if (userService.isExistsUserByEmail(user)) {
            return new ModelAndView(handleEmailExistsError(model, user));
        }
        if (user.getPassword().length() < 5) {
            return new ModelAndView(handlePasswordLengthError(model, user));
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            return new ModelAndView(handlePasswordMismatchError(model, user));
        }
        userService.create(user);
        return new ModelAndView("redirect:/users");
    }


    @GetMapping("/update")
    public ModelAndView update(HttpServletRequest request, Model model) {
        User user = userService.read(ObjectUtil.getId(request));
        addCommonAttributes(model, user);
        return new ModelAndView(TEMPLATE_ADMIN_USERS_FORM);
    }


    @PostMapping("/update")
    public ModelAndView updateUser(@ModelAttribute("user") @Valid User user, Model model) {
        User userBD = userService.read(user.getId());
        if (!userBD.getUsername().equals(user.getUsername())
                && userService.isExistsUserByUsername(user)) {
            return new ModelAndView(handleUsernameExistsError(model, user));
        }
        if (!userBD.getEmail().equals(user.getEmail())
                && userService.isExistsUserByEmail(user)) {
            return new ModelAndView(handleEmailExistsError(model, user));
        }
        userService.update(user, userBD.getId());
        return new ModelAndView("redirect:/users");
    }

    @GetMapping("/delete")
    public ModelAndView delete(HttpServletRequest request) {
        userService.delete(ObjectUtil.getId(request));
        return new ModelAndView("redirect:/users");
    }


    // Приватный метод для общей части логики добавления атрибутов модели
    private void addCommonAttributes(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("selectedRole", user.getUserRole());
    }

    // Приватный метод для обработки ошибки существующего имени пользователя
    public String handleUsernameExistsError(Model model, User user) {
        addCommonAttributes(model, user);
        UserHandler.usernameError(model);
        return TEMPLATE_ADMIN_USERS_FORM;
    }

    // Приватный метод для обработки ошибки существующего email
    private String handleEmailExistsError(Model model, User user) {
        addCommonAttributes(model, user);
        UserHandler.emailError(model);
        return TEMPLATE_ADMIN_USERS_FORM;
    }

    // Приватный метод для обработки ошибки неверной длины пароля
    private String handlePasswordLengthError(Model model, User user) {
        addCommonAttributes(model, user);
        UserHandler.passwordLengthError(model);
        return TEMPLATE_ADMIN_USERS_FORM;
    }

    // Приватный метод для обработки ошибки несовпадения паролей
    private String handlePasswordMismatchError(Model model, User user) {
        addCommonAttributes(model, user);
        UserHandler.passwordMatchError(model);
        return TEMPLATE_ADMIN_USERS_FORM;
    }
}

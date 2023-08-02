package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.controller.BaseController;
import com.topjava.votesystem.model.PasswordForm;
import com.topjava.votesystem.model.User;
import com.topjava.votesystem.service.UserService;
import com.topjava.votesystem.util.handlers.UserHandler;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.topjava.votesystem.util.handlers.UserHandler.isPasswordCorrect;


@Controller
@AllArgsConstructor
@RequestMapping(path = "/account")
public class AccountController extends BaseController {

    private final UserService userService;
    public final String ACCOUNT_TEMPLATE = "account";
    private final AuthenticationManager authenticationManager;


    @GetMapping()
    public String account(Principal principal, Model model) {
        User user = userService.readByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("passwordForm", new PasswordForm(false));
        return ACCOUNT_TEMPLATE;
    }

    @PostMapping()
    public String update(@ModelAttribute("user") @Valid User user,
                         @ModelAttribute PasswordForm passwordForm, Model model) {

        User userBD = userService.read(user.getId());
        boolean isUsernameChanged = !userBD.getUsername().equals(user.getUsername());

        if (isUsernameChanged && isUsernameOrEmailHasErrors(userBD, user, model)) {
            return ACCOUNT_TEMPLATE;
        }

        if (passwordForm.isShowPasswordCheckbox()) {
            if (!isPasswordValid(passwordForm, model)) {
                model.addAttribute("passwordForm", passwordForm);
                return ACCOUNT_TEMPLATE;
            } else {
                user.setPassword(passwordForm.getPassword());
            }
        }

        userService.update(user, userBD.getId());

        if (isUsernameChanged) {
            updateAuthentication(userBD);
        }
        model.addAttribute("successSave", true);
        return ACCOUNT_TEMPLATE;
    }

    private boolean isPasswordValid(PasswordForm passwordForm, Model model) {
        return isPasswordCorrect(passwordForm.getPassword(), passwordForm.getPasswordConfirm(), model);
    }

    private boolean isUsernameOrEmailHasErrors(User existingUser, User updatedUser, Model model) {
        if (userService.isExistsUserByUsername(updatedUser)) {
            UserHandler.usernameError(model);
            return true;
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            UserHandler.emailError(model);
            return true;
        }
        return false;
    }

    private void updateAuthentication(User user) {
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        Authentication reauthenticated = authenticationManager.authenticate(newAuthentication);
        SecurityContextHolder.getContext().setAuthentication(reauthenticated);
    }

}

package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.model.User;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping(value = ProfileRestController.REST_URL)
public class ProfileRestController extends UserAbstractController {
    static final String REST_URL = "/rest/account";

    private final MessageSource messageSource;

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<String> updateUser(@RequestBody User user, Locale locale) {

        User userBD = super.get(user.getId());

        boolean isUsernameChanged = !userBD.getUsername().equals(user.getUsername());
        boolean isEmailChanged = !userBD.getEmail().equals(user.getEmail());


        if (isUsernameChanged && isExistsUserByUsername(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.username.exists", null, locale));
        }

        if (isEmailChanged && isExistsUserByEmail(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("user.email.exists", null, locale));
        }

        if (!user.getPassword().isEmpty()) {
            if (user.getPassword().length() < 5) {
                return ResponseEntity.badRequest().body(
                        messageSource.getMessage("error.password.length", null, locale));
            }

            if (!user.getPassword().equals(user.getPasswordConfirm())) {
                return ResponseEntity.badRequest().body(
                        messageSource.getMessage("error.password.mismatch", null, locale));
            }
        }

        super.update(user, userBD.getId());

        if (isUsernameChanged || !user.getPassword().isEmpty()) {
            updateAuthentication(userBD);
        }

        return ResponseEntity.ok(
                messageSource.getMessage("success.save", null, locale));
    }

    private void updateAuthentication(User user) {
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        Authentication reauthenticated = authenticationManager.authenticate(newAuthentication);
        SecurityContextHolder.getContext().setAuthentication(reauthenticated);
    }
}
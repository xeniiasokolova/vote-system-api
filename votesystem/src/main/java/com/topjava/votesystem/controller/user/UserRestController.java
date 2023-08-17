package com.topjava.votesystem.controller.user;

import com.topjava.votesystem.model.User;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends UserAbstractController {
    static final String REST_URL = "/rest/users";

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(super.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = super.get(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody User user, Locale locale) {
        if (super.isExistsUserByUsername(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.username.exists", null, locale)
            );
        }
        if (super.isExistsUserByEmail(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("user.email.exists", null, locale)
            );
        }
        if (user.getPassword().length() < 5) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.password.length", null, locale)
            );
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.password.mismatch", null, locale)
            );
        }
        super.create(user);
        return ResponseEntity.ok(
                messageSource.getMessage("success.save", null, locale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user, Locale locale) {
        User userBD = super.get(user.getId());
        if (!userBD.getUsername().equals(user.getUsername())
                && isExistsUserByUsername(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.username.exists", null, locale));
        }

        if (!userBD.getEmail().equals(user.getEmail())
                && isExistsUserByEmail(user)) {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("user.email.exists", null, locale)
            );
        }
        super.update(user, id);
        return ResponseEntity.ok(
                messageSource.getMessage("success.save", null, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        super.delete(id);
        return ResponseEntity.ok().build();
    }
}

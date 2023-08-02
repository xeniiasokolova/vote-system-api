package com.topjava.votesystem.util.handlers;

import org.springframework.ui.Model;

public class UserHandler {
    public static void emailError(Model model) {
        model.addAttribute("emailExists", true);
    }

    public static void emailInvalidError(Model model) {
        model.addAttribute("emailError", "error.email.invalid");
    }

    public static void usernameError(Model model) {
        model.addAttribute("usernameExists", true);
    }

    public static void passwordLengthError(Model model) {
        model.addAttribute("passwordLengthError", "error.password.length");
    }

    public static void passwordMatchError(Model model) {
        model.addAttribute("passwordMatchError", "error.password.mismatch");
    }

    public static boolean isPasswordCorrect(String password, String passwordConfirm, Model model) {
        if (password.length() < 5) {
            passwordLengthError(model);
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            passwordMatchError(model);
            return false;
        }

        return true;
    }
}

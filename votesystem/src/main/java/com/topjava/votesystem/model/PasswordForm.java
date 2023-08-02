package com.topjava.votesystem.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {
    private boolean showPasswordCheckbox;
    private String password;
    private String passwordConfirm;

    public PasswordForm(boolean showPasswordCheckbox) {
      this.showPasswordCheckbox = showPasswordCheckbox;
    }

    public PasswordForm(String password, String passwordConfirm, boolean showPasswordCheckbox) {
        this.showPasswordCheckbox = showPasswordCheckbox;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}

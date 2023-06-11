package io.charla.users.communication.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


public class ChangeEmailDto {

    @NotNull(message = "{user.typo_email}")
    @Email()

    private String newEmail;

    @NotNull(message = "{user.password_require")
    private String password;

    public ChangeEmailDto() {
    }

    public ChangeEmailDto(String newEmail, String password) {
        this.newEmail = newEmail;
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

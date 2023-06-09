package io.charla.users.communication.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class HostDto {

    @NotNull(message = "{user.typo_email}")
    @Email()
    @Column(unique = true)
    private String email;

    @NotNull(message = "{user.typo_password}")
    @Size(min = 9, message = "{user.password_length}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message = "{user.password_criteria}")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

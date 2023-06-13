package io.charla.users.communication.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Getter
@Setter
public class ResetPassDto {


    @NotNull(message = "{user.typo_password}")
    @Size(min = 9, message = "{user.password_length}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message = "{user.password_criteria}")
    private String newPassword;

    @NotNull(message = "{user.typo_password}")
    @Size(min = 9, message = "{user.password_length}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message = "{user.password_criteria}")
    private String newPassConfirm;

    @NotNull(message = "{user.typo_email}")
    @Email()
    private String email;
}

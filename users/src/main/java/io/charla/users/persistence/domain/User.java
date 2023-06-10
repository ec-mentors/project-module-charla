package io.charla.users.persistence.domain;

import io.charla.users.persistence.domain.enums.Role;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @NotNull(message = "{user.typo_email}")
    @Email()
    @Column(unique = true)
    private String email;

    @NotNull(message = "{user.typo_password}")
    @Size(min = 9, message = "{user.password_length}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message = "{user.password_criteria}")
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{user.typo_role}")
    private Role role;

    @Column(unique = true)
    private String verificationCode;

    private boolean verified;

    public User() {
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    public long getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", verificationCode='" + verificationCode + '\'' +
                ", verified=" + verified +
                '}';
    }
}

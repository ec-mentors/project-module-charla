package io.charla.users.persistence.domain;

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
    @NotNull(message = "${messages.user-validation.typo-email}")
    @Email()
    @Column(unique = true)
    private String email;

    @NotNull(message = "${messages.user-validation.typo-password}")
    @Size(min = 9 , message = "${messages.user-validation.password-length}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message ="${messages.user-validation.password_criteria}")
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "${messages.user-validation.typo_role}")
    private Role role;
/*
todo Enum should be capial letter,
    @JsonProperty("Name"),
  public String name,
  remove unnecessary getter and setter,
  we should create another yaml file for error msg

 */

    @Column(unique = true)
    private String verificationCode;

    private boolean verified;

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

    public User() {
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
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
}

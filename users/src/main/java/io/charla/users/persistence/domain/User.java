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
    @NotNull(message = "you must type \"email\":")
    @Email()
    @Column(unique = true)
    private String email;

    @NotNull(message = "you must type \"password\":")
    @Size(min = 9 , message = "Password must be minimum 9")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit.")
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "you must type \"role\":")
    private Role role;
/*
todo Enum should be capial letter,
    @JsonProperty("Name"),
  public String name,
  remove unnecessary getter and setter


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

package io.charla.users.persistence.domain;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

@Entity
@Validated
public class HostUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Valid
    @OneToOne()
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<SafePlace> safePlaces;

    @OneToMany
    public Set<SafePlace> getSafePlaces() {
        return safePlaces;
    }

    public void setSafePlaces(Set<SafePlace> safePlaces) {
        this.safePlaces = safePlaces;
    }

    public HostUser() {
    }

    public HostUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return user.toString();
    }

}

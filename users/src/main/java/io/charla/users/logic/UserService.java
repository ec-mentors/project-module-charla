package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User signUp(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            //TODO to make sure the user is not overridden, but makes the validation obsolete
            throw new RuntimeException("email address already linked to account");
        } else {
            //TODO user should select role at front end from drop down box or something
            user.setAuthorities(List.of("ROLE_USER"));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }
}

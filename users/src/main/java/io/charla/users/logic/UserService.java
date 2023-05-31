package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.FindException;

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
            throw new DataIntegrityViolationException("email address already linked to account");
        } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
    }


}

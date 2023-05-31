package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final String tooShort;
    private final String noLower;
    private final String noUpper;
    private final String noNumber;
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, @Value("${errorMessages.tooShort}") String tooShort, @Value("${errorMessages.noLower}") String noLower, @Value("${errorMessages.noUpper}") String noUpper, @Value("${errorMessages.noNumber}") String noNumber) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tooShort = tooShort;
        this.noLower = noLower;
        this.noUpper = noUpper;
        this.noNumber = noNumber;
    }

    public User signUp(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            //TODO - to make sure the user is not overridden, but makes the validation obsolete
            throw new RuntimeException("email address already linked to account");
        } else {
            //TODO user should select role at front end from drop down box or something
            if (isPasswordValid(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
            //TODO - propagate error rather than having dummy return here
            return null;
        }
    }

    public boolean isPasswordValid(String password) {
        String errorMessage = "";
        boolean isTooShort = password.length() < 9;
        if (isTooShort) {
            errorMessage += tooShort;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            if (Character.isUpperCase(character)) {
                hasUpper = true;
            } else if (Character.isLowerCase(character)) {
                hasLower = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            }
        }
        if (!isTooShort && hasLower && hasUpper && hasNumber) {
            return true;
        } else {
            if (!hasLower) {
                errorMessage += noLower;
            }
            if (!hasUpper) {
                errorMessage += noUpper;
            }
            if (!hasNumber) {
                errorMessage += noNumber;
            }
            //TODO - should this be a runtime exception or something else?
            throw new RuntimeException(errorMessage);
        }
    }

}

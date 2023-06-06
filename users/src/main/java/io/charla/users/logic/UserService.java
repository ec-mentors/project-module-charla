package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Setter;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@ConfigurationProperties("messages.user-service")
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Setter(AccessLevel.PACKAGE)
    private String alreadyLinked, verificationSent, invalidCode, accountVerified, loggedIn, already_verified;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, EmailSenderService emailSenderService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
    }

    public String signUp(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException(alreadyLinked);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));


            boolean isUnique = false;
            String verificationCode;

            do {
                verificationCode = RandomString.make(64);
                isUnique = userRepository.findByVerificationCode(verificationCode).isPresent();
            } while (isUnique);


            user.setVerificationCode(verificationCode);


            User UserWithoutVerification = userRepository.save(user);

            emailSenderService.SendVerificationCode(UserWithoutVerification);


            return verificationSent;
        }
    }


    public String getVerified(String verificationCode) {

        User userHere = userRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new IllegalArgumentException(invalidCode));
        //TODO I dont think we need if here
        if (userHere.isVerified()) {
            return already_verified;
        } else {
            userHere.setVerificationCode(null);
            userHere.setVerified(true);
            userRepository.save(userHere);

            return accountVerified;
        }
    }


    public String login() {

        return loggedIn;

    }


}

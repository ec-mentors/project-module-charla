package io.charla.users.logic;

import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final EmailSenderService emailSenderService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, EmailSenderService emailSenderService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
    }

    public String signUp(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("Email address already linked to an account");
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


            return "Email has been sent to your email for verification";
        }
    }


    public String getVerified(String verificationCode) {

        User userHere = userRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new IllegalArgumentException("Invalid" + "\u200A" + " Verification code or expired"));
        //TODO I dont think we need if here
        if (userHere.isVerified()) {
            return "already verified";
        } else {
            userHere.setVerificationCode(null);
            userHere.setVerified(true);
            userRepository.save(userHere);

            return "Congratulation you are now verified. You can no login";
        }
    }


    public String login() {

        return "Finally you are logged in now";

    }


}

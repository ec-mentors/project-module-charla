package io.charla.users.logic;

import io.charla.users.persistence.domain.Role;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final StandardUserService standardUserService;
    private final EmailSenderService emailSenderService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, StandardUserService standardUserService, EmailSenderService emailSenderService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.standardUserService = standardUserService;
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
            assignUser(userHere);
            return "Congratulation you are now verified. You can now login";
        }
    }
    private void assignUser(User user) {
        if (user.getRole().equals(Role.ROLE_USER)){
            standardUserService.createStandardUser(user);
        } //TODO other user types
    }

    public String login() {

        return "Finally you are logged in now";

    }


}

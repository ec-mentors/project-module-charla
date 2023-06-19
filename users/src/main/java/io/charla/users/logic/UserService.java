package io.charla.users.logic;

import io.charla.users.communication.dto.ChangeEmailDto;
import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.ResetPassDto;
import io.charla.users.exception.PasswordException;
import io.charla.users.exception.UserNotFoundException;
import io.charla.users.persistence.domain.enums.Role;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import io.charla.users.security.ValidUserAccess;
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
    private final StandardUserService standardUserService;
    private final HostUserService hostUserService;
    private final EmailSenderServices emailSenderService;

    private final ValidUserAccess validUserAccess;
    @Setter(AccessLevel.PACKAGE)
    private String alreadyLinked,
            verificationSent,
            invalidCode,
            accountVerified,
            newEmailVerified,
            loggedIn,
            alreadyVerified,
            passwords_not_match,
            old_password_incorrect,
            password_incorrect,
            user_not_found,
            password_changed,
            userNotVerified,
            newPasswordConfirmed;


    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       StandardUserService standardUserService,
                       HostUserService hostUserService,
                       EmailSenderServices emailSenderService,
                       ValidUserAccess validUserAccess) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.standardUserService = standardUserService;
        this.hostUserService = hostUserService;
        this.emailSenderService = emailSenderService;
        this.validUserAccess = validUserAccess;
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


            User userWithoutVerification = userRepository.save(user);

            emailSenderService.SendVerificationCode(userWithoutVerification);


            return verificationSent;
        }
    }


    public String getVerified(String verificationCode) {

        User userHere = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new IllegalArgumentException(invalidCode));
        //TODO I dont think we need if here
        if (userHere.isVerified()) {
            return alreadyVerified;
        } else {
            userHere.setVerificationCode(null);
            userHere.setVerified(true);
            userRepository.save(userHere);
            assignUser(userHere);
            return accountVerified;

        }
    }


    private void assignUser(User user) {
        if (user.getRole().equals(Role.ROLE_USER)) {
            standardUserService.createStandardUser(user);
        } else { //TODO other user types

            // to create User creation (factory method) here
            hostUserService.createHostUser(user);
        }
    }

    public String login() {

        return loggedIn;

    }


    public String changePassword(ChangePasswordDto changePasswordDto, long userId) {
        validUserAccess.isValidUserAccess(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(user_not_found));

        if (passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            if (changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPasswordConfirm())) {
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

                userRepository.save(user);

                return password_changed;

            } else {
                throw new PasswordException(passwords_not_match);
            }
        }

        throw new PasswordException(old_password_incorrect);


    }

    public String changeEmail(ChangeEmailDto changeEmailDto, long userId) {
        validUserAccess.isValidUserAccess(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(user_not_found));

        if (passwordEncoder.matches(changeEmailDto.getPassword(), user.getPassword())) {

            boolean isUnique = false;
            String verificationCode;

            do {
                verificationCode = RandomString.make(64);
                isUnique = userRepository.findByVerificationCode(verificationCode).isPresent();
            } while (isUnique);


            user.setVerificationCode(verificationCode);
            user.setTempEmail(changeEmailDto.getNewEmail());

            User userWithoutVerification = userRepository.save(user);

            emailSenderService.SendVerificationCodeNewEmail(userWithoutVerification);


            return verificationSent;
        }

        throw new PasswordException(password_incorrect);
    }

    public String getNewEmailVerified(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new IllegalArgumentException(invalidCode));

        user.setEmail(user.getTempEmail());
        user.setTempEmail(null);
        user.setVerificationCode(null);

        userRepository.save(user);

        return newEmailVerified;
    }

    // TODO combine assignUser, use endpoint for change pass,email in user
    // TODO please let's get rid of id, when we are logged in, we see our info directly

    //TODO make only on email service class with different message and template passed on user input
    //TODO alla has changed wrong emailService
    //TODO let's solve DTO problem
    // TOTO New password should also have some validation


    public String resetUserPassword(ResetPassDto resetPassDto) {
        User user = userRepository.findByEmail(resetPassDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(user_not_found));

        if (!(user.isVerified())) {
            throw new IllegalStateException(userNotVerified);
        } else if (!(resetPassDto.getNewPassword().equals(resetPassDto.getNewPassConfirm()))) {
            throw new PasswordException(passwords_not_match);
        }else {

            String resetCode;
            do {
                resetCode = RandomString.make(64);
            } while (userRepository.findByVerificationCode(resetCode).isPresent());

            user.setResetCode(resetCode);
            user.setTempPassword(passwordEncoder.encode(resetPassDto.getNewPassConfirm()));
            userRepository.save(user);

            emailSenderService.sendRestCode(user);

            return verificationSent;
        }

    }


    public String confirmPasswordSe(String resetCode){

        User user = userRepository.findByResetCode(resetCode)
                .orElseThrow(() -> new IllegalStateException(invalidCode));

        user.setPassword(user.getTempPassword());

        user.setResetCode(null);
        user.setTempPassword(null);
        userRepository.save(user);

        return newPasswordConfirmed;
    }


}

package io.charla.users.logic;

import io.charla.users.communication.dto.HostDto;
import io.charla.users.persistence.domain.HostUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.HostUserRepository;
import io.charla.users.persistence.repository.UserRepository;
import io.charla.users.security.ValidUserAccess;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HostUserService {

    private final EmailSenderServicesHost emailSenderServicesHost;
    private final PasswordEncoder passwordEncoder;
    private final ValidUserAccess validUserAccess;
private final HostUserRepository hostUserRepository;
private final UserRepository userRepository;
    public HostUserService(EmailSenderServicesHost emailSenderServicesHost, PasswordEncoder passwordEncoder, ValidUserAccess validUserAccess, HostUserRepository hostUserRepository, UserRepository userRepository) {
        this.emailSenderServicesHost = emailSenderServicesHost;

        this.passwordEncoder = passwordEncoder;
        this.validUserAccess = validUserAccess;
        this.hostUserRepository = hostUserRepository;
        this.userRepository = userRepository;
    }


    public void createStandardUser(User user) {
        HostUser emptyHostUser = new HostUser();
        emptyHostUser.setUser(user);
        hostUserRepository.save(emptyHostUser);
    }
    // TODO, where there are more HostUser properties then implement HostDTO two combine both User and Host fields at one go
    public String editHostProfile(HostDto hostDto, Long id) {
        validUserAccess.isValidUserAccess(id);

        User foundUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("HostUser not found"));

        Optional<HostUser> relatedHostUser = hostUserRepository.findByUser(foundUser);


//        boolean isUnique = false;
//        String verificationCode;
//
//        do {
//            verificationCode = RandomString.make(64);
//            isUnique = userRepository.findByVerificationCode(verificationCode).isPresent();
//        } while (isUnique);
//        String finalVerificationCode = verificationCode;

        relatedHostUser.ifPresent(locatedHostUser -> {

//            locatedHostUser.getUser().setVerified(false);
//            locatedHostUser.getUser().setVerificationCode(finalVerificationCode);
//            emailSenderServicesHost.SendVerificationCode(locatedHostUser.getUser());


            locatedHostUser.getUser().setEmail(hostDto.getEmail());
            locatedHostUser.getUser().setPassword(passwordEncoder.encode(hostDto.getPassword()));

            hostUserRepository.save(locatedHostUser);

        });
        return "Your email changed successfully, please login with your new email";
    }






    public Optional<HostUser> retrieveHostProfileInfo(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

      return   hostUserRepository.findByUser(foundUser);
    }



}

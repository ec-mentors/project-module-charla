package io.charla.users.logic;

import io.charla.users.communication.dto.HostDto;
import io.charla.users.exception.MandatoryPropertyException;
import io.charla.users.persistence.domain.HostUser;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.HostUserRepository;
import io.charla.users.persistence.repository.SafePlaceRepository;
import io.charla.users.persistence.repository.UserRepository;
import io.charla.users.security.ValidUserAccess;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HostUserService {
    private final SafePlaceRepository safePlaceRepository;
    private final EmailSenderServicesHost emailSenderServicesHost;
    private final PasswordEncoder passwordEncoder;
    private final ValidUserAccess validUserAccess;
    private final HostUserRepository hostUserRepository;
    private final UserRepository userRepository;
    public HostUserService(SafePlaceRepository safePlaceRepository, EmailSenderServicesHost emailSenderServicesHost, PasswordEncoder passwordEncoder, ValidUserAccess validUserAccess, HostUserRepository hostUserRepository, UserRepository userRepository) {
        this.safePlaceRepository = safePlaceRepository;
        this.emailSenderServicesHost = emailSenderServicesHost;

        this.passwordEncoder = passwordEncoder;
        this.validUserAccess = validUserAccess;
        this.hostUserRepository = hostUserRepository;
        this.userRepository = userRepository;
    }
    //TODO Add safeplace translator function inside here so no logic is in the endpoint

    public Set<SafePlace> getUserSafePlaces(Long id) {
        HostUser hostUser = hostUserRepository.getById(id);
        validUserAccess.isValidUserAccess(hostUser.getUser().getId());
        return hostUser.getSafePlaces();
    }
    
    public SafePlace createSafePlace(SafePlace safePlace, Long id) {
//        if (safePlace.getName().isEmpty()) {
//            throw new MandatoryPropertyException("key \"name:\" is mandatory");
//        }
//        if (safePlace.getCountry() == null) {
//            throw new MandatoryPropertyException("key \"country:\" is mandatory");
//        }
//        if (safePlace.getCity() == null) {
//            throw new MandatoryPropertyException("key \"city:\" is mandatory");
//        }
        HostUser hostUser = hostUserRepository.getById(id);
        safePlace.setVievs(0);
        hostUser.getSafePlaces().add(safePlace);
        safePlaceRepository.save(safePlace);
        hostUserRepository.save(hostUser);
        return safePlace;
    }
//TODO should not we add User to the constructer of hostUser
    public void createHostUser(User user) {
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






    public String retrieveHostProfileInfo(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        HostUser host = hostUserRepository.findByUser(foundUser).orElseThrow(() -> new RuntimeException("User is not a host"));
        return host.toString();
    }



}

package io.charla.users.logic;

import io.charla.users.persistence.domain.StandardUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.StandardUserRepository;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StandardUserService {
    private final StandardUserRepository standardUserRepository;
    private final UserRepository userRepository;
    public StandardUserService(StandardUserRepository standardUserRepository, UserRepository userRepository) {
        this.standardUserRepository = standardUserRepository;
        this.userRepository = userRepository;
    }

    public void createStandardUser(User user) {
        StandardUser emptyStandardUser = new StandardUser();
        emptyStandardUser.setUser(user);
        standardUserRepository.save(emptyStandardUser);
    }

    public StandardUser editProfile(StandardUser standardUser, long userId) {
        //TODO - prevent users being able to change others' profiles based on Id
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        var oStandardUser = standardUserRepository.findByUser(user);
        if (oStandardUser.isEmpty()) {
            throw new RuntimeException("not found");
        } else {
            if (standardUser.getLanguages().isEmpty()) {
                throw new RuntimeException("languages mandatory");
            }
            if (standardUser.getCountry() == null) {
                throw new RuntimeException("country mandatory");
            }
            standardUser.setUser(user);
            standardUser.setId(oStandardUser.get().getId());
            return standardUserRepository.save(standardUser);
        }
    }
}

package io.charla.users.logic;

import io.charla.users.persistence.domain.StandardUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.StandardUserRepository;
import org.springframework.stereotype.Service;

@Service
public class StandardUserService {
    private final StandardUserRepository standardUserRepository;

    public StandardUserService(StandardUserRepository standardUserRepository) {
        this.standardUserRepository = standardUserRepository;
    }

    public void createStandardUser(User user) {
        StandardUser emptyStandardUser = new StandardUser();
        emptyStandardUser.setUser(user);
        standardUserRepository.save(emptyStandardUser);
    }

    public StandardUser editProfile(StandardUser standardUser, long userId) {
        //TODO - prevent users being able to change others' profiles based on Id
        var oStandardUser = standardUserRepository.findByUserId(userId);
        if (oStandardUser.isEmpty()) {
            throw new RuntimeException("not found");
        } else {
            if (standardUser.getLanguages().isEmpty()) {
                throw new RuntimeException("languages mandatory");
            }
            if (standardUser.getCountry() == null) {
                throw new RuntimeException("country mandatory");
            }
            return standardUserRepository.save(standardUser);
        }
    }
}

package io.charla.users.logic;


import io.charla.users.communication.dto.TopicScoreDto;
import io.charla.users.exception.MandatoryPropertyException;
import io.charla.users.exception.UserNotFoundException;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.StandardUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.domain.enums.Topic;
import io.charla.users.persistence.repository.SafePlaceRepository;
import io.charla.users.persistence.repository.StandardUserRepository;
import io.charla.users.persistence.repository.UserRepository;
import io.charla.users.security.ValidUserAccess;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StandardUserService {
    private final StandardUserRepository standardUserRepository;
    private final UserRepository userRepository;
    private final SafePlaceRepository safePlaceRepository;
    private final ValidUserAccess validUserAccess;
    public StandardUserService(StandardUserRepository standardUserRepository,
                               UserRepository userRepository,
                               SafePlaceRepository safePlaceRepository, ValidUserAccess validUserAccess) {
        this.standardUserRepository = standardUserRepository;
        this.userRepository = userRepository;
        this.safePlaceRepository = safePlaceRepository;
        this.validUserAccess = validUserAccess;
    }

    public StandardUser addSafePlace(long standardUserId, long safePlaceId) {
        StandardUser standardUser = getStandardUser(standardUserId);
        validUserAccess.isValidUserAccess(standardUser.getUser().getId());
        SafePlace safePlace = getSafePlace(safePlaceId);
        standardUser.getFavoriteSafePlaces().add(safePlace);
        standardUserRepository.save(standardUser);
        return standardUser;
    }

    public StandardUser removeSafePlace(long standardUserId, long safePlaceId) {
        StandardUser standardUser = getStandardUser(standardUserId);
        validUserAccess.isValidUserAccess(standardUser.getUser().getId());
        SafePlace safePlace = getSafePlace(safePlaceId);
        standardUser.getFavoriteSafePlaces().remove(safePlace);
        standardUserRepository.save(standardUser);
        return standardUser;
    }

    private SafePlace getSafePlace(long id) {
        Optional<SafePlace> oSafePlace = safePlaceRepository.findById(id);
        if (oSafePlace.isEmpty()) {
            throw new UserNotFoundException("safe place with id: " + id + " not found");
        }
        return oSafePlace.get();
    }

    private StandardUser getStandardUser(long id) {
        Optional<StandardUser> oStandardUser = standardUserRepository.findById(id);
        if (oStandardUser.isEmpty()) {
            throw new UserNotFoundException("user with id: " + id + " not found");
        }
        return oStandardUser.get();
    }

    public void createStandardUser(User user) {
        StandardUser emptyStandardUser = new StandardUser();
        emptyStandardUser.setUser(user);
        standardUserRepository.save(emptyStandardUser);
    }

    public StandardUser editProfile(StandardUser standardUser, long userId) {

        validUserAccess.isValidUserAccess(userId);

        if (standardUser.getLanguages().isEmpty()) {
            throw new MandatoryPropertyException("key \"languages:\" is mandatory");
        }
        if (standardUser.getCountry() == null) {
            throw new MandatoryPropertyException("key \"country:\" is mandatory");
        }
        StandardUser oldStandardUser = getStandardUserByUserId(userId);
        standardUser.setUser(getUser(userId));
        standardUser.setId(oldStandardUser.getId());

        standardUser.setTopicScoresMap(oldStandardUser.getTopicScoresMap());
        return standardUserRepository.save(standardUser);
        
    }
    public StandardUser modifyOpinions(long userId, TopicScoreDto topicScoreDto) {

        validUserAccess.isValidUserAccess(userId);

        int score = topicScoreDto.getAnswerOne() + topicScoreDto.getAnswerTwo() + topicScoreDto.getAnswerThree();
        StandardUser standardUser = getStandardUserByUserId(userId);
        standardUser.addTopicScore(Topic.valueOf(topicScoreDto.getTopic()), score);
        return standardUserRepository.save(standardUser);
        
    }
    
    private StandardUser getStandardUserByUserId(long userId){
        User user = getUser(userId);
        var oStandardUser = standardUserRepository.findByUser(user);
        if (oStandardUser.isEmpty()){
            throw new UserNotFoundException("user with id: " + userId+ " not found");
        }
        
        return oStandardUser.get();
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id: " + userId + " not found"));
    }
}

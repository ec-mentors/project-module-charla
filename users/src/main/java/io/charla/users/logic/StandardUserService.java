package io.charla.users.logic;


import io.charla.users.communication.dto.TopicScoreDto;
import io.charla.users.exception.MandatoryPropertyException;
import io.charla.users.exception.UserNotFoundException;
import io.charla.users.persistence.domain.StandardUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.domain.enums.Topic;
import io.charla.users.persistence.repository.StandardUserRepository;
import io.charla.users.persistence.repository.UserRepository;
import io.charla.users.security.ValidUserAccess;
import org.springframework.stereotype.Service;

@Service
public class StandardUserService {
    private final StandardUserRepository standardUserRepository;
    private final UserRepository userRepository;

    private final ValidUserAccess validUserAccess;
    public StandardUserService(StandardUserRepository standardUserRepository,
                               UserRepository userRepository,
                               ValidUserAccess validUserAccess) {
        this.standardUserRepository = standardUserRepository;
        this.userRepository = userRepository;
        this.validUserAccess = validUserAccess;
    }

    public void createStandardUser(User user) {
        StandardUser emptyStandardUser = new StandardUser();
        emptyStandardUser.setUser(user);
        standardUserRepository.save(emptyStandardUser);
    }

    public StandardUser editProfile(StandardUser standardUser, long userId) {
        //Done - prevent users being able to change others' profiles based on Id
        validUserAccess.isValidUserAccess(userId);
        //todo Should be replaced with validation, @NotEmpty can achieve same effect
        if (standardUser.getLanguages().isEmpty()) {
            throw new MandatoryPropertyException("key \"languages:\" is mandatory");
        }
        if (standardUser.getCountry() == null) {
            throw new MandatoryPropertyException("key \"country:\" is mandatory");
        }
        standardUser.setUser(getUser(userId));
        standardUser.setId(getStandardUserByUserId(userId).getId());
        return standardUserRepository.save(standardUser);
        
    }
    public StandardUser modifyOpinions(long userId, TopicScoreDto topicScoreDto) {
        //Done - prevent user being able to change others profile based on ID#
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

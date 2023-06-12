package io.charla.matcher.logic;

import io.charla.matcher.communication.dto.MatchPropertiesDto;
import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.LocationPreference;
import io.charla.matcher.persistance.domain.enums.Topic;
import io.charla.matcher.persistance.repository.StandardUserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MatchService {
    private final StandardUserRepository standardUserRepository;

    public MatchService(StandardUserRepository standardUserRepository) {
        this.standardUserRepository = standardUserRepository;
    }

    public StandardUser checkMatch(MatchPropertiesDto matchPropertiesDto) {
        //userId in dto is user or standardUser id?
        StandardUser standardUser = standardUserRepository.findById(matchPropertiesDto.getStandardUserId()).orElseThrow(() -> new RuntimeException("user not found"));

        //check mandatory fields are not null
        //check that profile is filled, if one is filled then all should be
        if (standardUser.getCountry() != null) {

            //check languages and topics from dto exist in profile
            //i did it like this instead of .containsAll so that the error can be specific to the specific language/topic
            for (Language language : matchPropertiesDto.getChosenLanguages()) {
                if (!standardUser.getLanguages().contains(language)) {
                    throw new RuntimeException(language + " is not listed in your profile, to match with someone using this language please update your profile, or reconsider whether you want to use this language to communicate");
                }
            }
            for (Topic topic : matchPropertiesDto.getChosenTopics()) {
                if (!standardUser.getPreferredTopics().contains(topic)) {
                    throw new RuntimeException("The topic " + topic + " is not listed in your preferred topics list, please update to continue");
                }
                //check that for chosen topics there are scores
                if (!standardUser.getTopicScoresMap().containsKey(topic)) {
                    throw new RuntimeException("You do not currently have a score available for The topic " + topic + ", please fill out the relevant questionnaire before continuing.");
                }
            }
            //location preferences are set as location based on profile, if pref == city then city should not be null
            if (matchPropertiesDto.getLocationPreference().equals(LocationPreference.MY_CITY)) {
                if (standardUser.getCity() == null) {
                    throw new RuntimeException("Please either select a city in your profile or choose another option for meeting");
                }
            }
        } else {
            throw new RuntimeException("Please complete your profile before searching for matches.");
        }
        return standardUser;
    }


    //start matching
    public Set<StandardUser> findMatches(MatchPropertiesDto matchPropertiesDto) {
        StandardUser standardUser = checkMatch(matchPropertiesDto);

        return Set.of(standardUser);
    }

}

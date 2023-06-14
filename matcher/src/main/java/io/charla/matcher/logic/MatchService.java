package io.charla.matcher.logic;

import io.charla.matcher.communication.dto.MatchPropertiesDto;
import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.LocationPreference;
import io.charla.matcher.persistance.domain.enums.Topic;
import io.charla.matcher.persistance.repository.StandardUserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final StandardUserRepository standardUserRepository;

    public MatchService(StandardUserRepository standardUserRepository) {
        this.standardUserRepository = standardUserRepository;
    }

    public StandardUser checkMatchIsReady(MatchPropertiesDto matchPropertiesDto) {
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
        //checks that match properties are shared by user profile
        StandardUser standardUser = checkMatchIsReady(matchPropertiesDto);

        //Set<StandardUser> matches = new HashSet<>();
        Set<StandardUser> potentialMatches2 = standardUserRepository.findAllByLanguagesIn(matchPropertiesDto.getChosenLanguages());

        Set<StandardUser> potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsIn(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics());

        //TODO - remove this
        System.out.println("all based on language: " + potentialMatches2);
        potentialMatches2.remove(standardUser);
        System.out.println("removed searching user: " + potentialMatches2);

        System.out.println("2all based on language and topic: " + potentialMatches);
        potentialMatches.remove(standardUser);
        System.out.println("2removed searching user: " + potentialMatches);

        Set<Topic> chosenTopics = matchPropertiesDto.getChosenTopics();
        for (StandardUser match : potentialMatches) {
            for (Topic topic : match.getTopicScoresMap().keySet()) {
                //potentialMatches = potentialMatches.stream().filter(potMatch-> matchPropertiesDto.getChosenTopics().contains(topic)).collect(Collectors.toSet());
                if (!chosenTopics.contains(topic)) {
                    potentialMatches.remove(match);
                }
            }
        }
        LocationPreference locationPreference = matchPropertiesDto.getLocationPreference();
        if (locationPreference == LocationPreference.MY_COUNTRY) {
            potentialMatches = potentialMatches.stream().filter(match -> match.getCountry() == standardUser.getCountry()).collect(Collectors.toSet());
        } else if (locationPreference == LocationPreference.MY_CITY) {
            potentialMatches = potentialMatches.stream().filter(match -> match.getCity() == standardUser.getCity()).collect(Collectors.toSet());
        }

        //find person with most shared topics (with chosenTopics)
        //if one then return
        //then calculate deltas per topic
        //then person with highest delta average

        //should just return match or also info about polarity etc.?

        //potentialMatches.stream().filter(match -> standardUser.getTopicScoresMap().containsKey(match.getTopicScoresMap().));

        //The algorithm has to consider the following aspects:
        //
        //Mandatory: if User 1 and 2 does not match here the match is “thrown away”
        //
        //Spoken language → both have to speak the same language
        //Preferred topics → both have to be interested in the same topics
        //Location → if searching person chooses e.g. “in my country” User 2 has to be within the same country
        //Dynamically:
        //
        //Opinion category→ both users have to disagree in the topics that are matching (e.g. User 1 is opinion category 1 and User 2 opinion category 3 in “Politics”)
        //In detail: Opposite opinions are preferred over similar opinions. If both have the same opinion categories the match is still not “thrown away”! (explicit message is shown in the frontend)
        //User 1 “Sabrina”: Searches for someone
        //
        //Location: Vienna
        //Language: English & German
        //Preferred Topics: Politics (2) and Religion (0)
        //User 2 “Hans”:
        //
        //Location: Vienna
        //Language: German
        //Preferred Topics: Politics (5) , Religion (3) and corona (10)
        //‌
        //User 3 “Gerhard”:
        //
        //Location: Graz
        //Language: Turkish
        //Preferred Topics: Religion (10)
        //Sabrina searches:
        //
        //Location: In my country
        //Language: German (English & German are suggested)
        //Preferred topic: Politics and Religion (Politics and Religion are suggested)
        //Result:
        //
        //Both are valid based on the location (both are in austria)
        //Hans matches but gerhard not (based on the language)
        //Both match based on the topics
        //Ranking might be necessary - if several topics are matched they are ranked above the difference
        //Hans: Poltics 3 and Religion 3 → 3 (average of all score deltas)
        //gerhard: Politics INVALID and Religion 10 → 10 (average of all score deltas)
        //Acceptance Criteria
        //Delete
        //0%
        //Top most match is returned as result (including e-mail address of match)
        //If no match was found an error message is thrown
        return potentialMatches;
    }

}

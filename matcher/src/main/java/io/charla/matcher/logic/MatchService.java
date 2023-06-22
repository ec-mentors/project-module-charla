package io.charla.matcher.logic;

import io.charla.matcher.communication.dto.MatchPropertiesDto;
import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.LocationPreference;
import io.charla.matcher.persistance.domain.enums.Topic;
import io.charla.matcher.persistance.repository.StandardUserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
    private final StandardUserRepository standardUserRepository;

    public MatchService(StandardUserRepository standardUserRepository) {
        this.standardUserRepository = standardUserRepository;
    }

    public StandardUser checkMatchIsReady(MatchPropertiesDto matchPropertiesDto) {
        //userId in dto is user or standardUser id?
        StandardUser standardUser = standardUserRepository.findById(matchPropertiesDto.getStandardUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));

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
    public List<StandardUser> findMatches(MatchPropertiesDto matchPropertiesDto) {
        //checks that match properties are shared by user profile
        StandardUser standardUser = checkMatchIsReady(matchPropertiesDto);


        //get a list of potential matches from the database matching language and preferred topic (and country or city if specified in matchPropertiesDto)
        Set<StandardUser> potentialMatches = Set.of();
        LocationPreference locationPreference = matchPropertiesDto.getLocationPreference();


        if (locationPreference == LocationPreference.MY_COUNTRY) {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsInAndCountry(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics(), standardUser.getCountry());
        } else if (locationPreference == LocationPreference.MY_CITY) {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsInAndCity(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics(), standardUser.getCity());
        } else {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsIn(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics());
        }
        //removes the searching user who should also be captured by those criteria
        potentialMatches.remove(standardUser);

        //filtering out the matches who have the topics but no completed questionnaires
        List<StandardUser> matches = new ArrayList<>();
        Set<Topic> chosenTopics = matchPropertiesDto.getChosenTopics();
        for (StandardUser match : potentialMatches) {
            for (Topic topic : match.getTopicScoresMap().keySet()) {
                if (chosenTopics.contains(topic)) {
                    matches.add(match);
                }
            }
        }

        //at this point we have our list of potential matches, if size == 0 or 1 then finished, if more than 1 then narrow down to best match.
        //this is based on the ticket as it currently stands (where we should return only the top match)
        if (matches.isEmpty()) {
            //return Optional.empty();
            //return List.of();
            throw new RuntimeException("Unfortunately, there were no matches found for those criteria");
        } else if (matches.size() == 1) {
            //TODO - change return value to Optional of instance not set of sUsers
            // will also need to change expectation of restTemplate in users service
            //return matches.stream().findFirst();
            return matches;
        }
        //TODO discuss with daniel what he actually wants here - according to the story it should follow the following process, but maybe not ideal
        //find person with most shared topics (with chosenTopics)
        //if one then return
        //then calculate deltas per topic
        //then return person with highest delta average. if tie then return one randomly?


        Map<StandardUser, Double> matchMeanAverageDeltaMap = new HashMap<>();
        Map<StandardUser, Map<Topic, Integer>> matchTopicDeltaMapMap = new HashMap<>();

        //calculating the delta for each topic of each match, as well as the average delta for each match across all common topics
        for (StandardUser match : matches) {
            matchTopicDeltaMapMap.put(match, new HashMap<>());
            int totalDelta = 0;
            for (Topic chosenTopic : chosenTopics) {
                if (match.getTopicScoresMap().containsKey(chosenTopic)) {
                    var delta = Math.abs(standardUser.getTopicScoresMap().get(chosenTopic) - match.getTopicScoresMap().get(chosenTopic));
                    matchTopicDeltaMapMap.get(match).put(chosenTopic, delta);
                    totalDelta += delta;
                }
            }
            var size = matchTopicDeltaMapMap.get(match).size();
            double averageDelta = (double) totalDelta / size;
            matchMeanAverageDeltaMap.put(match, averageDelta);
        }
        //ordering the maps based on the deltas in descending order
        var matchAverageEntryList = new ArrayList<>(matchMeanAverageDeltaMap.entrySet());
        matchAverageEntryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


        Map<StandardUser, Double> orderedMatchDeltaMap = new LinkedHashMap<>();
        for (var entry : matchAverageEntryList) {
            orderedMatchDeltaMap.put(entry.getKey(), entry.getValue());
        }
        //TODO - possibly change return value to Optional.of(top matched sUser), discuss with daniel as above
        return new ArrayList<>(orderedMatchDeltaMap.keySet());
    }

    public StandardUser findTopMatch(MatchPropertiesDto matchPropertiesDto) {
        return findMatches(matchPropertiesDto).get(0);
    }
}

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
    public List<StandardUser> findMatches(MatchPropertiesDto matchPropertiesDto) {
        //checks that match properties are shared by user profile
        StandardUser standardUser = checkMatchIsReady(matchPropertiesDto);

        Set<StandardUser> matches = new HashSet<>();
        //Set<StandardUser> potentialMatches2 = standardUserRepository.findAllByLanguagesIn(matchPropertiesDto.getChosenLanguages());

        Set<StandardUser> potentialMatches = Set.of();
        LocationPreference locationPreference = matchPropertiesDto.getLocationPreference();
        if (locationPreference == LocationPreference.MY_COUNTRY) {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsInAndCountry(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics(), standardUser.getCountry());
        } else if (locationPreference == LocationPreference.MY_CITY) {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsInAndCity(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics(), standardUser.getCity());
        } else {
            potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsIn(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics());
        }
        potentialMatches.remove(standardUser);

//        Set<StandardUser> potentialMatches = standardUserRepository.findAllByLanguagesInAndPreferredTopicsIn(matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics());

        //TODO - remove this
//        System.out.println("all based on language: " + potentialMatches2);
//        potentialMatches2.remove(standardUser);
//        System.out.println("removed searching user: " + potentialMatches2);
        System.out.println("2all based on language and topic (and maybe country or city): " + potentialMatches);

        Set<Topic> chosenTopics = matchPropertiesDto.getChosenTopics();
        for (StandardUser match : potentialMatches) {
            for (Topic topic : match.getTopicScoresMap().keySet()) {
                //potentialMatches = potentialMatches.stream().filter(potMatch-> matchPropertiesDto.getChosenTopics().contains(topic)).collect(Collectors.toSet());
                if (chosenTopics.contains(topic)) {
                    matches.add(match);
                }
            }
        }

//        LocationPreference locationPreference = matchPropertiesDto.getLocationPreference();
//        if (locationPreference == LocationPreference.MY_COUNTRY) {
//            matches = matches.stream().filter(match -> match.getCountry() == standardUser.getCountry()).collect(Collectors.toSet());
//        } else if (locationPreference == LocationPreference.MY_CITY) {
//            matches = matches.stream().filter(match -> match.getCity() == standardUser.getCity()).collect(Collectors.toSet());
//        }

        if (matches.isEmpty()) {
            //return Optional.empty();
            return List.of();
        } else if (matches.size() == 1) {
            //TODO - change return value to Optional of instance not set of sUsers
            // will also need to change expectation of restTemplate in users service
            //return matches.stream().findFirst();
        }
        //TODO ---------- at this point we have our list of potential matches, if size == 0 or 1 then finished, if more than 1 then narrow down to best match
        //find person with most shared topics (with chosenTopics)
        //if one then return
        //then calculate deltas per topic
        //then return person with highest delta average. if tie then return one randomly?

        Map<StandardUser, Double> matchMeanAverageDeltaMap = new HashMap<>();
        Map<StandardUser, Map<Topic, Integer>> matchTopicDeltaMapMap = new HashMap<>();

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
            //Map<Topic, Integer> topicDeltaMap = new HashMap<>();
        }

        System.out.println(matchMeanAverageDeltaMap);
        var matchAverageEntryList = new ArrayList<>(matchMeanAverageDeltaMap.entrySet());
        matchAverageEntryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        System.out.println(matchAverageEntryList);
        System.out.println("this is either best or worst match: " + matchAverageEntryList.get(0).getKey());

        Map<StandardUser, Double> orderedMatchDeltaMap = new LinkedHashMap<>();
        for (var entry : matchAverageEntryList) {
            orderedMatchDeltaMap.put(entry.getKey(), entry.getValue());
        }
//        List<StandardUser> matchesOrdered = new ArrayList<>();
//        matchesOrdered.addAll(orderedMatchDeltaMap.keySet());

        return new ArrayList<>(orderedMatchDeltaMap.keySet());

        //TODO should just return match or also info about polarity etc.?
    }

}

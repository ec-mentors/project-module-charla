package io.charla.matcher.logic;

import io.charla.matcher.communication.dto.MatchPropertiesDto;
import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.User;
import io.charla.matcher.persistance.domain.enums.Country;
import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.LocationPreference;
import io.charla.matcher.persistance.domain.enums.Topic;
import io.charla.matcher.persistance.repository.StandardUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MatchServiceTest {
    @Autowired
    MatchService matchService;
    @MockBean
    StandardUserRepository standardUserRepository;
    @MockBean
    SecurityFilterChain securityFilterChain;

    @ParameterizedTest
    @MethodSource("paramsForCheck")
    void checkMatch(MatchPropertiesDto matchPropertiesDto, StandardUser expectedStandardUser, Map<Topic, Integer> topicScoreMap) {
        Mockito.when(standardUserRepository.findById(matchPropertiesDto.getStandardUserId())).thenReturn(Optional.of(new StandardUser(new User(), matchPropertiesDto.getChosenLanguages(), matchPropertiesDto.getChosenTopics(), Country.AUSTRIA, null, topicScoreMap)));
         var actual = matchService.checkMatch(matchPropertiesDto);
         actual.setId(1);
         Assertions.assertEquals(expectedStandardUser, actual);
    }
    @ParameterizedTest
    @MethodSource("paramsForCheckFail")
    void checkMatch_fails(MatchPropertiesDto matchPropertiesDto, StandardUser standardUser, Set<Language> languages, Set<Topic> topics) {
        standardUser.setLanguages(languages);
        standardUser.setPreferredTopics(topics);
        Mockito.when(standardUserRepository.findById(matchPropertiesDto.getStandardUserId())).thenReturn(Optional.of(standardUser));
        Assertions.assertThrows(RuntimeException.class, () -> {
            matchService.checkMatch(matchPropertiesDto);
        });
    }
    @Test
    void findMatches() {
    }

    private static Stream<Arguments> paramsForCheck() {
        Map<Topic, Integer> topicScoreMap = new HashMap<>();
        topicScoreMap.put(Topic.POLITICS, 3);
        topicScoreMap.put(Topic.ENVIRONMENT, 4);

        StandardUser standardUser = new StandardUser(new User(),Set.of(Language.SERBIAN), Set.of(Topic.POLITICS), Country.AUSTRIA, null, topicScoreMap);
        standardUser.setId(1);

        MatchPropertiesDto matchPropertiesDto = new MatchPropertiesDto((long) 1, LocationPreference.ANYWHERE, Set.of(Topic.POLITICS),Set.of(Language.SERBIAN));
        return Stream.of(
                Arguments.of(matchPropertiesDto, standardUser, topicScoreMap),
                Arguments.of(),
                Arguments.of(),
                Arguments.of()
        );
    }
    private static Stream<Arguments> paramsForCheckFail() {
        Map<Topic, Integer> topicScoreMap = new HashMap<>();
        topicScoreMap.put(Topic.POLITICS, 3);
        topicScoreMap.put(Topic.ENVIRONMENT, 4);

        Set<Language> someMissingLanguages = Set.of(Language.GERMAN, Language.TURKISH, Language.HUNGARIAN);
        Set<Language> allLanguages = Set.of(Language.GERMAN, Language.TURKISH, Language.HUNGARIAN, Language.CROATIAN, Language.ENGLISH, Language.SERBIAN);
        Set<Topic> allTopics = Set.of(Topic.POLITICS, Topic.ENVIRONMENT,Topic.CORONA, Topic.RELIGION);
        Set<Topic> someMissingTopics = Set.of(Topic.POLITICS, Topic.ENVIRONMENT);


        StandardUser userNotFound = new StandardUser();
        StandardUser emptyStandardUser = new StandardUser();
        emptyStandardUser.setId(1);


        StandardUser standardUser = new StandardUser(new User(),Set.of(Language.SERBIAN), Set.of(Topic.POLITICS), Country.AUSTRIA, null, topicScoreMap);
        standardUser.setId(1);

        MatchPropertiesDto matchPropertiesDto = new MatchPropertiesDto((long) 1, LocationPreference.ANYWHERE, Set.of(Topic.POLITICS, Topic.CORONA),Set.of(Language.SERBIAN, Language.GERMAN));
        MatchPropertiesDto matchPropertiesDtoMyCity = new MatchPropertiesDto((long) 1, LocationPreference.MY_CITY, Set.of(Topic.POLITICS, Topic.ENVIRONMENT),Set.of(Language.SERBIAN, Language.GERMAN));
        return Stream.of(
                Arguments.of(matchPropertiesDto, userNotFound, allLanguages, allTopics),
                Arguments.of(matchPropertiesDto, emptyStandardUser, allLanguages, allTopics),
                Arguments.of(matchPropertiesDto, standardUser, someMissingLanguages, allTopics),
                Arguments.of(matchPropertiesDto, standardUser, allLanguages, someMissingTopics),
                Arguments.of(matchPropertiesDtoMyCity, standardUser, allLanguages, allTopics)
        );
    }
}
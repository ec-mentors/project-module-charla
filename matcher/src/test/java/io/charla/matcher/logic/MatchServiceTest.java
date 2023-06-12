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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MatchServiceTest {
    @Autowired
    MatchService matchService;
    @MockBean
    StandardUserRepository standardUserRepository;


    @ParameterizedTest
    @MethodSource("paramsForCheck")
    void checkMatch(MatchPropertiesDto matchPropertiesDto, StandardUser standardUser) {

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
        StandardUser standardUser = new StandardUser();
        return Stream.of(
                Arguments.of(),
                Arguments.of(),
                Arguments.of(),
                Arguments.of()
        );
    }
    private static Stream<Arguments> paramsForCheckFail() {
        Map<Topic, Integer> topicScoreMap = new HashMap<>();
        topicScoreMap.put(Topic.POLITICS, 3);
        topicScoreMap.put(Topic.ENVIRONMENT, 4);

        StandardUser emptyStandardUser = new StandardUser();
        StandardUser standardUser = new StandardUser(new User(),Set.of(Language.GERMAN, Language.TURKISH, Language.HUNGARIAN), Set.of(Topic.POLITICS), Country.AUSTRIA, null, topicScoreMap);
        standardUser.setId(1);

        MatchPropertiesDto matchPropertiesDto = new MatchPropertiesDto((long) 1, LocationPreference.ANYWHERE, Set.of(Topic.POLITICS, Topic.ENVIRONMENT),Set.of(Language.SERBIAN, Language.GERMAN));
        return Stream.of(
                Arguments.of(matchPropertiesDto, emptyStandardUser),
                Arguments.of(matchPropertiesDto, standardUser),
                Arguments.of(),
                Arguments.of()
        );
    }
}
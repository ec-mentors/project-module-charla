package io.charla.users.configuration;

import io.charla.users.persistence.domain.HostUser;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.StandardUser;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.domain.enums.*;
import io.charla.users.persistence.repository.HostUserRepository;
import io.charla.users.persistence.repository.SafePlaceRepository;
import io.charla.users.persistence.repository.StandardUserRepository;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class DatabaseFilling {
    @Bean
    ApplicationRunner prepareDatabaseForFirstTime(UserRepository userRepository,
                                                  StandardUserRepository standardUserRepository,
                                                  HostUserRepository hostUserRepository,
                                                  SafePlaceRepository safePlaceRepository,
                                                  PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = new User("blabla1@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user1.setVerified(true);
                userRepository.save(user1);

                StandardUser standardUser1 = new StandardUser(user1,
                        Set.of(Language.ENGLISH, Language.GERMAN),
                        Set.of(Topic.POLITICS, Topic.RELIGION),
                        Country.AUSTRIA,
                        City.VIENNA,
                        Map.of(Topic.POLITICS, 12, Topic.RELIGION, 0));
                standardUserRepository.save(standardUser1);

                User user2 = new User("blabla2@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user2.setVerified(true);
                userRepository.save(user2);

                StandardUser standardUser2 = new StandardUser(user2,
                        Set.of(Language.ENGLISH, Language.TURKISH, Language.CROATIAN),
                        Set.of(Topic.POLITICS, Topic.ENVIRONMENT),
                        Country.AUSTRIA,
                        null,
                        Map.of(Topic.POLITICS, 0, Topic.ENVIRONMENT, 0));
                standardUserRepository.save(standardUser2);

                User user3 = new User("blabla3@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user3.setVerified(true);
                userRepository.save(user3);

                StandardUser standardUser3 = new StandardUser(user3,
                        Set.of(Language.ENGLISH, Language.GERMAN, Language.TURKISH),
                        Set.of(Topic.POLITICS, Topic.ENVIRONMENT, Topic.RELIGION),
                        Country.AUSTRIA,
                        City.VIENNA,
                        Map.of(Topic.POLITICS, 0, Topic.ENVIRONMENT, 0, Topic.RELIGION, 5));
                standardUserRepository.save(standardUser3);

                User user4 = new User("blabla4@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user4.setVerified(true);
                userRepository.save(user4);

                StandardUser standardUser4 = new StandardUser(user4,
                        Set.of(Language.ENGLISH, Language.GERMAN, Language.TURKISH),
                        Set.of(Topic.POLITICS, Topic.ENVIRONMENT, Topic.RELIGION),
                        Country.AUSTRIA,
                        City.GRAZ,
                        Map.of(Topic.ENVIRONMENT, 0, Topic.RELIGION, 5));
                standardUserRepository.save(standardUser4);

                User user5 = new User("blabla5@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user5.setVerified(true);
                userRepository.save(user5);

                StandardUser standardUser5 = new StandardUser(user5,
                        Set.of(Language.TURKISH),
                        Set.of(Topic.RELIGION),
                        Country.AUSTRIA,
                        null,
                        Map.of(Topic.RELIGION, 12));
                standardUserRepository.save(standardUser5);

                User user6 = new User("blabla6@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user6.setVerified(true);
                userRepository.save(user6);

                StandardUser standardUser6 = new StandardUser(user6,
                        Set.of(Language.ENGLISH, Language.TURKISH, Language.HUNGARIAN),
                        Set.of(Topic.RELIGION, Topic.CORONA, Topic.POLITICS),
                        Country.AUSTRIA,
                        City.VIENNA,
                        Map.of(Topic.RELIGION, 8, Topic.CORONA, 10, Topic.POLITICS, 3));
                standardUserRepository.save(standardUser6);

                User user7 = new User("blabla7@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user7.setVerified(true);
                userRepository.save(user7);

                StandardUser standardUser7 = new StandardUser(user7,
                        Set.of(Language.TURKISH, Language.ENGLISH, Language.GERMAN, Language.SERBIAN, Language.CROATIAN, Language.HUNGARIAN),
                        Set.of(Topic.RELIGION, Topic.POLITICS, Topic.CORONA, Topic.ENVIRONMENT),
                        Country.AUSTRIA,
                        City.VIENNA,
                        Map.of(Topic.RELIGION, 2, Topic.POLITICS, 4, Topic.CORONA, 10, Topic.ENVIRONMENT, 7));
                standardUserRepository.save(standardUser7);

                User user8 = new User("blabla8@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user8.setVerified(true);
                userRepository.save(user8);

                StandardUser standardUser8 = new StandardUser(user8,
                        Set.of(Language.HUNGARIAN),
                        Set.of(Topic.CORONA),
                        Country.AUSTRIA,
                        City.INNSBRUCK,
                        Map.of(Topic.CORONA, 11));
                standardUserRepository.save(standardUser8);

                User user9 = new User("blabla9@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_USER);
                user9.setVerified(true);
                userRepository.save(user9);

                StandardUser standardUser9 = new StandardUser(user9,
                        Set.of(Language.TURKISH, Language.ENGLISH, Language.GERMAN),
                        Set.of(Topic.RELIGION, Topic.CORONA, Topic.ENVIRONMENT),
                        Country.AUSTRIA,
                        City.SALZBERG,
                        Map.of(Topic.RELIGION, 6, Topic.CORONA, 5, Topic.ENVIRONMENT, 8));
                standardUserRepository.save(standardUser9);

                List<SafePlace> safePlaces = List.of(
                        new SafePlace("Place 1", Country.AUSTRIA, City.VIENNA, List.of(SafePlaceKeywords.CAFE)),
                        new SafePlace("Place 2", Country.AUSTRIA, City.SALZBERG, List.of(SafePlaceKeywords.QUIET)),
                        new SafePlace("Place 3", Country.AUSTRIA, City.GRAZ, List.of(SafePlaceKeywords.CAFE, SafePlaceKeywords.QUIET)),
                        new SafePlace("Place 4", Country.AUSTRIA, City.VIENNA, List.of(SafePlaceKeywords.LGBTQ, SafePlaceKeywords.BALKONY)),
                        new SafePlace("Place 5", Country.AUSTRIA, City.INNSBRUCK, List.of())

                );

                safePlaceRepository.saveAll(safePlaces);

                User user10 = new User("blabla10@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_HOST);
                user10.setVerified(true);
                userRepository.save(user10);

                HostUser hostUser10 = new HostUser(user10,
                        Set.of(
                                safePlaces.get(0), safePlaces.get(1), safePlaces.get(2)
                        )
                );

                hostUserRepository.save(hostUser10);

                User user11 = new User("blabla11@gmail.com", passwordEncoder.encode("passworD1"), Role.ROLE_HOST);
                user11.setVerified(true);
                userRepository.save(user11);

                HostUser hostUser11 = new HostUser(user11,
                        Set.of(
                                safePlaces.get(3), safePlaces.get(4)
                        )
                );

                hostUserRepository.save(hostUser11);
            }

        };
    }
}
package io.charla.matcher.persistance.repository;


import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.User;
import io.charla.matcher.persistance.domain.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface StandardUserRepository extends JpaRepository<StandardUser, Long> {
    Optional<StandardUser> findByUser(User user);
    Set<StandardUser> findAllByLanguagesIn(Set<Language> languages);
    //TODO - in this "In" works then can be used with "And" in one query
}

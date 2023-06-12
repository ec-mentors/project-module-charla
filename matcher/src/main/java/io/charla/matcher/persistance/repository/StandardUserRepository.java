package io.charla.matcher.persistance.repository;


import io.charla.matcher.persistance.domain.StandardUser;
import io.charla.matcher.persistance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StandardUserRepository extends JpaRepository<StandardUser, Long> {
    Optional<StandardUser> findByUser(User user);
}

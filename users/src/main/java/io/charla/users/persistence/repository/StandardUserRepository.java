package io.charla.users.persistence.repository;

import io.charla.users.persistence.domain.StandardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StandardUserRepository extends JpaRepository<StandardUser, Long> {
    Optional<StandardUser> findByUserId(long id);
}

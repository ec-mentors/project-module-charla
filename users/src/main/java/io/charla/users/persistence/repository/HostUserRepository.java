package io.charla.users.persistence.repository;

import io.charla.users.persistence.domain.HostUser;
import io.charla.users.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostUserRepository extends JpaRepository<HostUser,Long> {

    Optional<HostUser> findByUser(User user);

    @Override
    HostUser getById(Long aLong);
}

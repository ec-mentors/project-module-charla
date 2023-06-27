package io.charla.users.persistence.repository;

import io.charla.users.persistence.domain.SafePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafePlaceRepository extends JpaRepository<SafePlace, Long> {

}

package io.charla.users.persistence.repository;

import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.enums.City;
import io.charla.users.persistence.domain.enums.Country;
import io.charla.users.persistence.domain.enums.SafePlaceKeywords;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SafePlaceRepository extends JpaRepository<SafePlace, Long> {

    List<SafePlace> findAll(Specification<SafePlace> specification, Pageable ofSize);
}

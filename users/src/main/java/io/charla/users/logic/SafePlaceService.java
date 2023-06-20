package io.charla.users.logic;

import io.charla.users.communication.dto.SafePlaceDTO;
import io.charla.users.communication.dto.SafePlaceResultDto;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.enums.City;
import io.charla.users.persistence.domain.enums.Country;
import io.charla.users.persistence.domain.enums.SafePlaceKeywords;
import io.charla.users.persistence.repository.HostUserRepository;
import io.charla.users.persistence.repository.SafePlaceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SafePlaceService {

    private final SafePlaceRepository safePlaceRepository;
    private final HostUserRepository hostUserRepository;

    public SafePlaceService(SafePlaceRepository safePlaceRepository, HostUserRepository hostUserRepository) {
        this.safePlaceRepository = safePlaceRepository;
        this.hostUserRepository = hostUserRepository;
    }

    public SafePlace safePlaceDTOTranslator(SafePlaceDTO safePlaceDTO) {
        List<SafePlaceKeywords> safePlaceKeywords = new ArrayList<>();
        safePlaceDTO.getKeywords().forEach(s -> safePlaceKeywords.add(SafePlaceKeywords.valueOf(s)));
        return new SafePlace(safePlaceDTO.getName(), Country.valueOf(safePlaceDTO.getCountry()), City.valueOf(safePlaceDTO.getCity()), safePlaceKeywords);
    }

    public List<SafePlaceResultDto> searchForSafePlacesResult(SafePlaceDTO safePlaceDTO,int limit) {
        System.out.println(safePlaceDTO);
        System.out.println(limit);
        List<SafePlace> safePlaces = search(safePlaceDTO,limit);
        return safePlaces.stream().map(x -> SafePlaceResultDto.translateToResult(x, hostUserRepository.findBySafePlaces(x).orElse(null))).collect(Collectors.toList());
    }

    public List<SafePlace> search(SafePlaceDTO safePlaceDTO,int limit){
        Specification<SafePlace> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (safePlaceDTO.getName() != null){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" +
                                safePlaceDTO.getName().toLowerCase() + "%")
                );
            }

            if (safePlaceDTO.getCountry() != null) {
                predicates.add(criteriaBuilder.equal(root.get("country"), Country.valueOf(safePlaceDTO.getCountry())));
            }

            if (safePlaceDTO.getCity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), City.valueOf(safePlaceDTO.getCity())));
            }

            if (safePlaceDTO.getKeywords() != null) {
                predicates.add(root.joinList("keywords").in(safePlaceDTO.getKeywords().stream()
                        .map(SafePlaceKeywords::valueOf).collect(Collectors.toList())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

        return safePlaceRepository.findAll(specification, Pageable.ofSize(limit > 0 ? limit : 10));
    }

}

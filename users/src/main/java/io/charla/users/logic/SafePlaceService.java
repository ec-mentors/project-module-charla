package io.charla.users.logic;

import io.charla.users.communication.dto.SafePlaceDTO;
import io.charla.users.exception.UserNotFoundException;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.enums.City;
import io.charla.users.persistence.domain.enums.Country;
import io.charla.users.persistence.domain.enums.SafePlaceKeywords;
import io.charla.users.persistence.repository.SafePlaceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SafePlaceService {

    private final SafePlaceRepository safePlaceRepository;


    public SafePlaceService(SafePlaceRepository safePlaceRepository) {
        this.safePlaceRepository = safePlaceRepository;
    }

    public SafePlace safePlaceDTOTranslator(SafePlaceDTO safePlaceDTO) {
        List<SafePlaceKeywords> safePlaceKeywords = new ArrayList<>();
        safePlaceDTO.getKeywords().forEach(s -> safePlaceKeywords.add(SafePlaceKeywords.valueOf(s)));
        return new SafePlace(safePlaceDTO.getName(),
                Country.valueOf(safePlaceDTO.getCountry()),
                City.valueOf(safePlaceDTO.getCity()),
                safePlaceKeywords);
    }

    public void increaseViews(Long id) {
        Optional<SafePlace> safePlace = safePlaceRepository.findById(id);
        if (safePlace.isEmpty()) {
            throw new UserNotFoundException("Safe place not found!");
        }
        safePlace.get().setViews(safePlace.get().getViews() + 1);
    }
}

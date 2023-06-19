package io.charla.users.logic;

import io.charla.users.communication.dto.SafePlaceDTO;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.domain.enums.City;
import io.charla.users.persistence.domain.enums.Country;
import io.charla.users.persistence.domain.enums.SafePlaceKeywords;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SafePlaceService {
    public SafePlace safePlaceDTOTranslator(SafePlaceDTO safePlaceDTO) {
        List<SafePlaceKeywords> safePlaceKeywords = new ArrayList<>();
        safePlaceDTO.getKeywords().forEach(s -> safePlaceKeywords.add(SafePlaceKeywords.valueOf(s)));
        return new SafePlace(safePlaceDTO.getName(),
                Country.valueOf(safePlaceDTO.getCountry()),
                City.valueOf(safePlaceDTO.getCity()),
                safePlaceKeywords
                );
    }
}

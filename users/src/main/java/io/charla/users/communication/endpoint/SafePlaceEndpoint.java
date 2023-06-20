package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.SafePlaceDTO;
import io.charla.users.communication.dto.SafePlaceResultDto;
import io.charla.users.logic.SafePlaceService;
import io.charla.users.persistence.domain.SafePlace;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("safe-places")
public class SafePlaceEndpoint {

    private final SafePlaceService safePlaceService;


    public SafePlaceEndpoint(SafePlaceService safePlaceService) {
        this.safePlaceService = safePlaceService;
    }

    @PostMapping("/search")
    @Secured("ROLE_USER")
    List<SafePlaceResultDto> searchForSafePlaces(@RequestBody SafePlaceDTO safePlaceDTO, @RequestParam("limit") int limit){
        return safePlaceService.searchForSafePlacesResult(safePlaceDTO,limit);
    }
}

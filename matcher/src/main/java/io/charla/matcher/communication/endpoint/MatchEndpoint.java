package io.charla.matcher.communication.endpoint;

import io.charla.matcher.communication.dto.MatchPropertiesDto;
import io.charla.matcher.logic.MatchService;
import io.charla.matcher.persistance.domain.StandardUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/match")
public class MatchEndpoint {
    private final MatchService matchService;

    public MatchEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }
    @PostMapping
    Set<StandardUser> findMatches(@Valid @RequestBody MatchPropertiesDto matchPropertiesDto) {
        return matchService.findMatches(matchPropertiesDto);
    }

    //TODO - //CLIENT IN USER
    //    //set of users in findmatch
    //    test
}

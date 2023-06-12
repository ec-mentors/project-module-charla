package io.charla.users.communication.client;

import io.charla.users.communication.dto.MatchPropertiesDto;
import io.charla.users.persistence.domain.StandardUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Controller
public class MatcherClient {
    private final RestTemplate restTemplate;

    public MatcherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public Set<StandardUser> findMatches(MatchPropertiesDto matchPropertiesDto) {
        return Set.of(restTemplate.postForObject("http://localhost:9002/match", matchPropertiesDto, StandardUser[].class));
    }
}
package io.charla.users.communication.client;

import io.charla.users.communication.dto.MatchPropertiesDto;
import io.charla.users.persistence.domain.StandardUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class MatcherClient {
    private final RestTemplate restTemplate;

    public MatcherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<StandardUser> findMatches(MatchPropertiesDto matchPropertiesDto) {
        return List.of(restTemplate.postForObject("http://localhost:9002/matches", matchPropertiesDto, StandardUser[].class));
    }
    public StandardUser findTopMatch(MatchPropertiesDto matchPropertiesDto) {
        return restTemplate.postForObject("http://localhost:9002/top-match", matchPropertiesDto, StandardUser.class);
    }
}

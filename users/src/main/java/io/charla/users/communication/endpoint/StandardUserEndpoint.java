package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.TopicScoreDto;
import io.charla.users.logic.StandardUserService;
import io.charla.users.persistence.domain.StandardUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/standard-users")
public class StandardUserEndpoint {
    private final StandardUserService standardUserService;

    public StandardUserEndpoint(StandardUserService standardUserService) {
        this.standardUserService = standardUserService;
    }

    @PutMapping("/edit-profile/{id}")
    @Secured("ROLE_USER")
    StandardUser editProfile(@RequestBody StandardUser standardUser, @PathVariable long id) {
        return standardUserService.editProfile(standardUser, id);
    }
    @PutMapping("/add-score/{id}")
    @Secured("ROLE_USER")
    StandardUser addScore(@Valid @RequestBody TopicScoreDto topicScoreDto, @PathVariable long id) {
        return standardUserService.modifyOpinions(id, topicScoreDto);
    }
}
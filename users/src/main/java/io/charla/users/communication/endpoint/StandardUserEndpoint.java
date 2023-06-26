package io.charla.users.communication.endpoint;


import io.charla.users.communication.client.MatcherClient;
import io.charla.users.communication.dto.ChangeEmailDto;
import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.MatchPropertiesDto;
import io.charla.users.communication.dto.TopicScoreDto;
import io.charla.users.logic.SafePlaceService;
import io.charla.users.logic.StandardUserService;
import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.StandardUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/standard-users")
public class StandardUserEndpoint {
    private final StandardUserService standardUserService;
    private final SafePlaceService safePlaceService;
    private final UserService userService;
    private final MatcherClient matcherClient;

    public StandardUserEndpoint(StandardUserService standardUserService, SafePlaceService safePlaceService, MatcherClient matcherClient, UserService userService) {
        this.standardUserService = standardUserService;
        this.safePlaceService = safePlaceService;
        this.matcherClient = matcherClient;
        this.userService = userService;
    }

    @PutMapping("/{standardUserId}/view/{safePlaceId}")
    @Secured("ROLE_USER")
    void viewSafePlace(@PathVariable long safePlaceId) {
        safePlaceService.increaseViews(safePlaceId);
    }
    @PutMapping("/{standardUserId}/add/{safePlaceId}")
    @Secured("ROLE_USER")
    StandardUser addSafePlace(@PathVariable long standardUserId, @PathVariable long safePlaceId) {
        return standardUserService.addSafePlace(standardUserId, safePlaceId);
    }

    @PutMapping("/{standardUserId}/remove/{safePlaceId}")
    @Secured("ROLE_USER")
    StandardUser removeSafePlace(@PathVariable long standardUserId, @PathVariable long safePlaceId) {
        return standardUserService.removeSafePlace(standardUserId, safePlaceId);
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

    @GetMapping("/match")
    @Secured("ROLE_USER")
    List<StandardUser> getMatches(@RequestBody MatchPropertiesDto matchPropertiesDto) {
        return matcherClient.findMatches(matchPropertiesDto);
    }
    @GetMapping("/match-one")
    @Secured("ROLE_USER")
    StandardUser getMatch(@RequestBody MatchPropertiesDto matchPropertiesDto) {
        var a = matcherClient.findMatches(matchPropertiesDto);
        if (a.isEmpty()) {
            return null;
        }
        return a.get(0);
    }

    @PutMapping("/change-password/{id}")
    @Secured("ROLE_USER")
    String changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,@PathVariable long id){
        return userService.changePassword(changePasswordDto,id);
    }

    @PutMapping("/change-email/{id}")
    @Secured("ROLE_USER")
    String changeEmail(@Valid @RequestBody ChangeEmailDto changeEmailDto,@PathVariable long id){
        return userService.changeEmail(changeEmailDto,id);
    }
}

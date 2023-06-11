package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.ChangeEmailDto;
import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.TopicScoreDto;
import io.charla.users.logic.StandardUserService;
import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.StandardUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/standard-users")
public class StandardUserEndpoint {
    private final StandardUserService standardUserService;
    private final UserService userService;

    public StandardUserEndpoint(StandardUserService standardUserService,
                                UserService userService) {
        this.standardUserService = standardUserService;
        this.userService = userService;
    }
//todo modify edit-profile to edit-profiles and add scores
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

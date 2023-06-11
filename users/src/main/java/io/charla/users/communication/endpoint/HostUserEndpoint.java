package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.ChangeEmailDto;
import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.HostDto;
import io.charla.users.logic.HostUserService;
import io.charla.users.logic.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/host-users")
public class HostUserEndpoint {

    private final HostUserService hostUserService;
    private final UserService userService;

    public HostUserEndpoint(HostUserService hostUserService, UserService userService) {
        this.hostUserService = hostUserService;
        this.userService = userService;
    }

    @GetMapping("/get-profile/{id}")
    public String getHostProfileData(@PathVariable Long id) {

        return hostUserService.retrieveHostProfileInfo(id);
    }

    @PutMapping("/change-password/{id}")
    @Secured("ROLE_HOST")
    String changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                          @PathVariable long id){
        return userService.changePassword(changePasswordDto,id);
    }

    @PutMapping("/change-email/{id}")
    @Secured("ROLE_HOST")
    String changeEmail(@Valid @RequestBody ChangeEmailDto changeEmailDto
                        ,@PathVariable long id){
        return userService.changeEmail(changeEmailDto,id);
    }

}

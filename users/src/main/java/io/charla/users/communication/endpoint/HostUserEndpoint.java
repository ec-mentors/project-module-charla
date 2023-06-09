package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.HostDto;
import io.charla.users.exception.UserNotFoundException;
import io.charla.users.logic.HostUserService;
import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.HostUser;
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
//todo Sandra taught us to throw exception here, our service code should return optional
    // use validation

    @PutMapping("/edit-profiles/{id}")
    @Secured("ROLE_HOST")
    public String updateHostProfile(@Valid @RequestBody HostDto hostDto, @PathVariable Long id) {

        return hostUserService.editHostProfile(hostDto, id);
    }


    @GetMapping("/get-profiles/{id}")
    public void getHostProfileData(@PathVariable Long id) {

        hostUserService.retrieveHostProfileInfo(id).orElseThrow(() -> new UserNotFoundException("User not found"));

    }


    @GetMapping("/verify-edit-profile")
    public String verified(@RequestParam("code") String verificationCode) {
        return userService.approveProfileEdit(verificationCode);
    }

}

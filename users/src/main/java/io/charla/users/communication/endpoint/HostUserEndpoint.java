package io.charla.users.communication.endpoint;

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
//todo Sandra taught us to throw exception here, our service code should return optional
    // use validation

    @PutMapping("/edit-profile/{id}")
    @Secured("ROLE_HOST")
    public String updateHostProfile(@Valid @RequestBody HostDto hostDto, @PathVariable Long id) {

        return hostUserService.editHostProfile(hostDto, id);
    }


    @GetMapping("/get-profile/{id}")
    public String getHostProfileData(@PathVariable Long id) {

        return hostUserService.retrieveHostProfileInfo(id);
    }


    @GetMapping("/verify-edit-profile")
    public String verified(@RequestParam("code") String verificationCode) {
        return userService.approveProfileEdit(verificationCode);
    }

}

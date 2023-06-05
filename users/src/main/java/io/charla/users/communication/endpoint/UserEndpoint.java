package io.charla.users.communication.endpoint;

import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/login", "/login/"})
    String loginUser() {
        return userService.login();
    }


    @PostMapping("/signup")
    String signUp(@Valid @RequestBody User user) {
        return userService.signUp(user);
    }

    @GetMapping("/verify")
    public String verified(@RequestParam("code") String verificationCode) {

        return userService.getVerified(verificationCode);
    }





//  TODO  Entrypoint is fot any path it should be only for login path ,hide id when signing up , should we use enum or list of authorities , use manual password critira check instead of regex


}

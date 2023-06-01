package io.charla.users.communication.endpoint;

import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Secured({"ROLE_USER","ROLE_HOST"})
    String loginUser() {
        return "logged in successfully";
    }


    @PostMapping("/signup")
    User signUp(@Valid @RequestBody User user) {
        return userService.signUp(user);
    }


//  TODO  Entrypoint is fot any path it should be only for login path ,hide id when signing up , should we use enum or list of authorities , use manual password critira check instead of regex




}

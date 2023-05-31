package io.charla.users.communication.endpoint;

import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    //@Secured({"ROLE_USER","ROLE_HOST"})
    String loginUser() {
        return "logged in successfully";
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login() {
//
//        if (loginSuccessful) {
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }

    @PostMapping("/signup")
    User signUp(@RequestBody User user) {
        return userService.signUp(user);
    }
// todo we should add Postmapping for login checking

// todo customise validation exception , hide if when signing up , should we use enum or list of authorities




}

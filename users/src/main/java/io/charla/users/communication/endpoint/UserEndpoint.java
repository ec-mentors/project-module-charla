package io.charla.users.communication.endpoint;

import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }


    //TODO - should we change req-mappings (and antMatchers)? should vis-mods be public or package?
    //PostMapping instead of Get for security - discuss with Nichirvan
    @GetMapping("/login") //   e.g. "/login"

    public String loginUser() {

        return "logged in successfully";
    }

    @PostMapping("/signup")
    User signUp(User user) {
        return userService.signUp(user);
    }


}

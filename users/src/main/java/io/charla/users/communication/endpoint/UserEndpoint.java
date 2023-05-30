package io.charla.users.communication.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserEndpoint {


    @GetMapping("/users")
    public String loginUser() {

        return "logged in successfully";
    }


}

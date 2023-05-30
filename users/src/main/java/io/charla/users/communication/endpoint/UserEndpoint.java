package io.charla.users.communication.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserEndpoint {


    @PostMapping("/login")
    public String loginUser() {

        return "logged in successfully";
    }


}

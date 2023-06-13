package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.ResetPassDto;
import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.User;
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

    @GetMapping("/verify-new-email")
    public String verifyNewEmail(@RequestParam("code") String verificationCode){
        return userService.getNewEmailVerified(verificationCode);
    }


    @PostMapping("/reset-password")
    public String forgetPassword(@Valid @RequestBody ResetPassDto resetPassDto){
        return userService.resetUserPassword(resetPassDto);
    }


    @GetMapping("/new-password")
    public String newPassword( @RequestParam("code") String resetCode){
        return userService.confirmPasswordSe(resetCode);
    }


}

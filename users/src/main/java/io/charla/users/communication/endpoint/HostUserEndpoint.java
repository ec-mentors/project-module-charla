package io.charla.users.communication.endpoint;

import io.charla.users.communication.dto.ChangeEmailDto;
import io.charla.users.communication.dto.ChangePasswordDto;
import io.charla.users.communication.dto.SafePlaceDTO;
import io.charla.users.logic.HostUserService;
import io.charla.users.logic.SafePlaceService;
import io.charla.users.logic.UserService;
import io.charla.users.persistence.domain.SafePlace;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/host-users")
public class HostUserEndpoint {
    private final SafePlaceService safePlaceService;
    private final HostUserService hostUserService;
    private final UserService userService;

    public HostUserEndpoint(SafePlaceService safePlaceService, HostUserService hostUserService, UserService userService) {
        this.safePlaceService = safePlaceService;
        this.hostUserService = hostUserService;
        this.userService = userService;
    }
    @PostMapping("/create-safe-place/{id}")
    @Secured("ROLE_HOST")
    SafePlace createSafePlace(@Valid @RequestBody SafePlaceDTO safePlaceDTO, @PathVariable Long id) {
        SafePlace safePlace = safePlaceService.safePlaceDTOTranslator(safePlaceDTO);
        hostUserService.createSafePlace(safePlace, id);
        return safePlace;
    }

    @GetMapping("/get-safeplace/{id}")
    @Secured("ROLE_HOST")
    public Set<SafePlace> getSafePlaces(@PathVariable Long id) {
        return hostUserService.getUserSafePlaces(id);
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

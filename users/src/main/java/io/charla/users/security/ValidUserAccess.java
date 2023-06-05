package io.charla.users.security;

import io.charla.users.exception.ForbiddenUserAccessException;
import io.charla.users.persistence.domain.User;
import io.charla.users.persistence.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidUserAccess {
    private Object principle;
    private final UserRepository userRepository;

    public ValidUserAccess(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * this method will assign a UserDetails Object from authentication credentials to the principle Object
     * if the credentials is valid we will get a UserDetails
     * otherwise we will get dummy object
     */
    private void setUserPrinciple(){
        this.principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * this method will check if the id of the user that coming from the authentication
     * is the same as the id that we provided as param
     * if the ids matches nothing will happen, and we can continue
     * otherwise an ForbiddenUserAccessException will be thrown.
     * @param id this should be the id from the url
     */
    public void isValidUserAccess(long id){
        setUserPrinciple();
        // get a user by the id we provided
        Optional<User> oUser = userRepository.findById(id);

        // checking if the principle object is instance of UserDetails
        // this mean we got the user from the authentication credential
        if (principle instanceof UserDetails){
            UserPrincipal userPrincipal = (UserPrincipal) principle;
            // we check if the id from user coming from authentication is the same as the id we provided (from url)
            if (oUser.filter(user -> userPrincipal.getUserId() == user.getId()).isPresent()){
                // we do nothing
                return;
            }
        }
        // if the check failed we throw a ForbiddenUserAccessException
        throw new ForbiddenUserAccessException("you are not allowed to access here");
    }
}

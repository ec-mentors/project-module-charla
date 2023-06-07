package io.charla.users.configuration;

import io.charla.users.exception.ForbiddenUserAccessException;
import io.charla.users.exception.MandatoryPropertyException;
import io.charla.users.exception.UserNotFoundException;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;


@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties("messages.custom-exception-handler")
@RestControllerAdvice
public class CustomExceptionHandler implements AuthenticationEntryPoint {

    private String wrongCredentials, notVerified, loginWithoutCredentials, authEnabledNoEndPoint, noEndpoint, typoInRole;


    /**
     * when user use wrong verification code an exception will be thrown.
     * this method is responsible to handle it and show only the custom msg not full Trace
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }


    /**
     * this method handle Authentication Exception which may occur when you use wrong credential or access non-existence endpoint.
     * the method handle 4 cases
     * 1. when the User try to log in without credential at all
     * 2. when the User try to log in with "wrong" credential
     * 3. when the User enter correct credential but he/she is not yet verified
     * 4. when the User go to wrong endpoint or non-existence endpoint // because Spring security restrict access all over the app, so we had to handle non-existence endpoint here two. we have also handled non-existence endpoint without spring security. See down there
     */

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        String errorMessage;
//
//        if (request.getRequestURI().equals("/users/login") || request.getRequestURI().equals("/users/login/")
//                || request.getRequestURI().equals("/standard-users/add-score/2")
//        ) {
//            if (authException instanceof BadCredentialsException) {
//                errorMessage = wrongCredentials;
//            } else if (authException instanceof DisabledException) {
//                errorMessage = notVerified;
//            } else {
//                errorMessage = loginWithoutCredentials;
//            }
//        } else {
//            errorMessage = authEnabledNoEndPoint;
//        }
//
//        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write(errorMessage);
//    }
//todo if user try to access non exist endpoint with wrong credential his password is saved
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String errorMessage;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")

        ) {
            if (authException instanceof BadCredentialsException) {
                errorMessage = wrongCredentials;
            } else if (authException instanceof DisabledException) {
                errorMessage = notVerified;
            } else {
                errorMessage = loginWithoutCredentials;
            }
        } else {
            errorMessage = authEnabledNoEndPoint;
        }

        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
    }


    /**
     * this method is used to handle 404 not found error msg and return custom msg instead.
     * To make this method work we must tell Spring Boot to throw exception when the User try to access non-existence endpoint (by default Spring Boot only return error msg). we do that by adding the following to yaml file  mvc:
     * spring:
     * mvc:
     * throw-exception-if-no-handler-found: true
     * web:
     * resources:
     * add-mappings: false
     */

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFoundError(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEndpoint);
    }


    /**
     * this exception is for email, but I have not test it
     */
    @ExceptionHandler({MessagingException.class, UnsupportedEncodingException.class})
    public ResponseEntity<String> emailErrors(Exception ex) {
        if (ex instanceof MessagingException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());

        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
        }
    }


    /**
     * custom msg for both validation and email uniqueness and enum role typo
     */

    @ExceptionHandler({DataIntegrityViolationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> validationErrors(Exception ex) {
        if (ex instanceof DataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } else if (ex instanceof MethodArgumentNotValidException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(((MethodArgumentNotValidException) ex).getFieldError()).getDefaultMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * this will handel the custom ForbiddenUserAccessException (created by us)
     * the ForbiddenUserAccessException will occur when a user try to edit or change another user profile
     * @param e
     * @return String message with 403 status code
     */
    @ExceptionHandler(ForbiddenUserAccessException.class)
    public ResponseEntity<String> validUserAccess(Exception e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * this will handel the custom UserNotFoundException (created by us)
     * the UserNotFoundException will occur when a user with a specific id doesn't exist
     * @param e
     * @return String message with 404 status code
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * this will handel the custom MandatoryPropertyException (created by us)
     * the MandatoryPropertyException will occur when mandatory property is messing;
     * @param e
     * @return String message with 400 status code
     */
    @ExceptionHandler(MandatoryPropertyException.class)
    public ResponseEntity<String> mandatoryProperty(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> testAll(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("workd");
    }
}

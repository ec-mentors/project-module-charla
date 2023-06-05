package io.charla.users.exception;

public class ForbiddenUserAccessException extends RuntimeException {
    public ForbiddenUserAccessException(String s) {
        super(s);
    }
}

package io.charla.users.exception;

public class MandatoryPropertyException extends RuntimeException {
    public MandatoryPropertyException(String message) {
        super(message);
    }
}

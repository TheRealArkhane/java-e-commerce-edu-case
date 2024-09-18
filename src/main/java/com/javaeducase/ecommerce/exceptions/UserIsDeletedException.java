package com.javaeducase.ecommerce.exceptions;

public class UserIsDeletedException extends RuntimeException{

    public UserIsDeletedException() {
        super();
    }

    public UserIsDeletedException(String message) {
        super(message);
    }

    public UserIsDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsDeletedException(Throwable cause) {
        super(cause);
    }
}


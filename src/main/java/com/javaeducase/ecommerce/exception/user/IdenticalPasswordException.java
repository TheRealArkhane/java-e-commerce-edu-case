package com.javaeducase.ecommerce.exception.user;

public class IdenticalPasswordException extends RuntimeException {

    public IdenticalPasswordException(String message) {
        super(message);
    }
}
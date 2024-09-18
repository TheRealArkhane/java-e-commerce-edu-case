package com.javaeducase.ecommerce.exceptions.user;

public class IdenticalPasswordException extends RuntimeException {

    public IdenticalPasswordException(String message) {
        super(message);
    }
}
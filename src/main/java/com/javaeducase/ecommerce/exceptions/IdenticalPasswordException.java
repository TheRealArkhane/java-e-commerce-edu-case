package com.javaeducase.ecommerce.exceptions;

public class IdenticalPasswordException extends RuntimeException {

    public IdenticalPasswordException() {super();}

    public IdenticalPasswordException(String message) {
        super(message);
    }
}
package com.javaeducase.ecommerce.exceptions.user;

public class UserIsDeletedException extends RuntimeException{

    public UserIsDeletedException(String message) {
        super(message);
    }

}


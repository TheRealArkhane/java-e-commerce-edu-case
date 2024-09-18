package com.javaeducase.ecommerce.exceptions.user;

public class InsufficientAdminPrivilegesException extends RuntimeException {

    public InsufficientAdminPrivilegesException(String message) {
        super(message);
    }
}

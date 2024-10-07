package com.javaeducase.ecommerce.exception.user;

public class InsufficientAdminPrivilegesException extends RuntimeException {

    public InsufficientAdminPrivilegesException(String message) {
        super(message);
    }
}

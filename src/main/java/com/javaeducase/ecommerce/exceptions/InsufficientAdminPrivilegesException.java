package com.javaeducase.ecommerce.exceptions;

public class InsufficientAdminPrivilegesException extends RuntimeException {
    public InsufficientAdminPrivilegesException(String message) {
        super(message);
    }
}

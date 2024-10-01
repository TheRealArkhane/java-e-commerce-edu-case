package com.javaeducase.ecommerce.exceptions.product;

public class DuplicateAttributeException extends RuntimeException {
    public DuplicateAttributeException(String message) {
        super(message);
    }
}

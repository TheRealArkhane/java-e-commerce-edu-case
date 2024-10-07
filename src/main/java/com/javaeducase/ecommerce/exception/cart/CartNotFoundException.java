package com.javaeducase.ecommerce.exception.cart;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String message) {
        super(message);
    }
}

package com.javaeducase.ecommerce.exceptions.cart;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}

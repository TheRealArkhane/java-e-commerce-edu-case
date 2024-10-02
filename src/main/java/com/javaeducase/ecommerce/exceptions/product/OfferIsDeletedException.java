package com.javaeducase.ecommerce.exceptions.product;

public class OfferIsDeletedException extends RuntimeException {
    public OfferIsDeletedException(String message) {
        super(message);
    }
}

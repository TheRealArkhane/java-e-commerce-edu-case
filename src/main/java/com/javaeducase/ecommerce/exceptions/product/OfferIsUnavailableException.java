package com.javaeducase.ecommerce.exceptions.product;

public class OfferIsUnavailableException extends RuntimeException {
    public OfferIsUnavailableException(String message) {
        super(message);
    }
}

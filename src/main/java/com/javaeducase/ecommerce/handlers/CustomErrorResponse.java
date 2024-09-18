package com.javaeducase.ecommerce.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse {
    private String message;
    private int status;

    public CustomErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}

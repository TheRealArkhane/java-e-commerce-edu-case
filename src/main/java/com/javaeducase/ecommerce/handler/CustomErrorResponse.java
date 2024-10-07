package com.javaeducase.ecommerce.handler;

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

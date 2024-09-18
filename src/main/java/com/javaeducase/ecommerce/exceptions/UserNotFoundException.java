package com.javaeducase.ecommerce.exceptions;

public class UserNotFoundException extends RuntimeException {

    // Конструктор по умолчанию
    public UserNotFoundException() {
        super();
    }

    // Конструктор с сообщением
    public UserNotFoundException(String message) {
        super(message);
    }

    // Конструктор с сообщением и причиной
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Конструктор с причиной
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}

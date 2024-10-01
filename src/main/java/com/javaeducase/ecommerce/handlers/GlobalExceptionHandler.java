package com.javaeducase.ecommerce.handlers;

import com.javaeducase.ecommerce.exceptions.product.CategoryNotFoundException;
import com.javaeducase.ecommerce.exceptions.product.DuplicateAttributeException;
import com.javaeducase.ecommerce.exceptions.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.exceptions.user.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exceptions.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IdenticalPasswordException.class)
    public ResponseEntity<CustomErrorResponse> handleIdenticalNewPassword(IdenticalPasswordException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsDeletedException.class)
    public ResponseEntity<CustomErrorResponse> handleUserIsDeleted(UserIsDeletedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentsExceptions(IllegalArgumentException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpRequestMethodNotSupportedExceptions(HttpRequestMethodNotSupportedException ex) {
        String HttpRequestMethodNotSupportedExceptionMessage = "HTTP метод " + ex.getMethod() + " не поддерживается";
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpRequestMethodNotSupportedExceptionMessage, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientAdminPrivilegesException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedExceptions(InsufficientAdminPrivilegesException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCategoryNotFoundExceptions(CategoryNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateAttributeException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicateAttributeExceptions(DuplicateAttributeException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

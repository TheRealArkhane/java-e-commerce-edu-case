package com.javaeducase.ecommerce.handler;

import com.javaeducase.ecommerce.exception.cart.CartItemNotFoundException;
import com.javaeducase.ecommerce.exception.cart.CartNotFoundException;
import com.javaeducase.ecommerce.exception.order.DeliveryNotFoundException;
import com.javaeducase.ecommerce.exception.order.PaymentNotFoundException;
import com.javaeducase.ecommerce.exception.product.*;
import com.javaeducase.ecommerce.exception.user.IdenticalPasswordException;
import com.javaeducase.ecommerce.exception.user.InsufficientAdminPrivilegesException;
import com.javaeducase.ecommerce.exception.user.UserIsDeletedException;
import com.javaeducase.ecommerce.exception.user.UserNotFoundException;
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

    @ExceptionHandler(ProductIsDeletedException.class)
    public ResponseEntity<CustomErrorResponse> handleProductIsDeleted(ProductIsDeletedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalStateExceptions(IllegalStateException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OfferIsDeletedException.class)
    public ResponseEntity<CustomErrorResponse> handleOfferIsDeleted(OfferIsDeletedException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OfferIsUnavailableException.class)
    public ResponseEntity<CustomErrorResponse> handleOfferIsUnavailableExceptions(OfferIsUnavailableException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCartItemNotFoundExceptions(CartItemNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCartNotFoundExceptions(CartItemNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleDeliveryNotFoundExceptions(DeliveryNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handlePaymentNotFoundExceptions(PaymentNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

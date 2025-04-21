package com.backend.cartservice.exception;

public class CartEmptyException extends RuntimeException {

    public CartEmptyException() {
        super("Cart is empty. Cannot proceed to checkout.");
    }

    public CartEmptyException(String message) {
        super(message);
    }
}
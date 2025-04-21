package com.backend.cartservice.exception;

public class CartFullException extends RuntimeException {

    public CartFullException() {
        super("Cart item limit exceeded.");
    }

    public CartFullException(String message) {
        super(message);
    }
}

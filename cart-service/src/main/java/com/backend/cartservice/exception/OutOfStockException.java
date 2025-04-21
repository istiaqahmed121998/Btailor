package com.backend.cartservice.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException() {
        super("Requested item is out of stock.");
    }

    public OutOfStockException(String message) {
        super(message);
    }
}

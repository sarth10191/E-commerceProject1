package com.example.demo.Exceptions;

public class WishListNotFoundException extends RuntimeException {
    public WishListNotFoundException(String message) {
        super(message);
    }
}

package com.codewithmosh.store.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String s) {
        super(s);
    }
}

package com.codewithmosh.store.feature.cart.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String s) {
        super(s);
    }
}

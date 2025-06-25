package com.codewithmosh.store.feature.cart.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String s) {
        super(s);
    }
}

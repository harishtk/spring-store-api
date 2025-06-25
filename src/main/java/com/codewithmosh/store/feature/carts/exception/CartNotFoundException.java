package com.codewithmosh.store.feature.carts.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String s) {
        super(s);
    }
}

package com.codewithmosh.store.feature.carts.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String s) {
        super(s);
    }
}

package com.codewithmosh.store.feature.payments.core.exception;


/**
 * Exception thrown when payment processing encounters an error.
 * This runtime exception is used to handle various payment-related failures
 * in the store application.
 */
public class PaymentException extends RuntimeException {
    /**
     * Constructs a new PaymentException with the specified error message.
     *
     * @param s the detail message explaining the reason for the payment failure
     */
    public PaymentException(String s) {
        super(s);
    }
}

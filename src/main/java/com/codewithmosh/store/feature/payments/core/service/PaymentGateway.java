package com.codewithmosh.store.feature.payments.core.service;

import com.codewithmosh.store.feature.orders.entity.Order;
import com.codewithmosh.store.feature.payments.core.model.CheckoutSession;
import com.codewithmosh.store.feature.payments.core.model.PaymentResult;
import com.codewithmosh.store.feature.payments.core.model.WebHookRequest;

import java.util.Optional;

/**
 * Interface defining the contract for payment gateway implementations.
 * Provides methods for creating checkout sessions and handling webhook responses
 * from payment providers.
 */
public interface PaymentGateway {
    /**
     * Creates a new checkout session for the given order.
     *
     * @param order The order for which to create a checkout session
     * @return A CheckoutSession object containing the necessary information for payment processing
     */
    CheckoutSession createCheckoutSession(Order order);

    /**
     * Parses a webhook request from the payment provider and returns the payment result.
     *
     * @param request The webhook request containing payment status information
     * @return Optional containing the PaymentResult if the webhook request was successfully parsed,
     * empty Optional otherwise
     */
    Optional<PaymentResult> parseWebhookRequest(WebHookRequest request);
}

package com.codewithmosh.store.feature.payments.provider.stripe.service;

import com.codewithmosh.store.feature.orders.entity.Order;
import com.codewithmosh.store.feature.orders.entity.OrderItem;
import com.codewithmosh.store.core.models.PaymentStatus;
import com.codewithmosh.store.feature.payments.core.model.CheckoutSession;
import com.codewithmosh.store.feature.payments.core.exception.PaymentException;
import com.codewithmosh.store.feature.payments.core.service.PaymentGateway;
import com.codewithmosh.store.feature.payments.core.model.PaymentResult;
import com.codewithmosh.store.feature.payments.core.model.WebHookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhook-secret-key}")
    private String webhookSecret;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/success.htm")
                    .setCancelUrl(websiteUrl + "/cancel.htm");

            order.getItems().forEach(item -> sessionBuilder.addLineItem(createLineItem(item)));
            Session session = Session.create(sessionBuilder.build());

            return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            throw new PaymentException("Could not create checkout session");
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebHookRequest request) {
        try {
            var signature = request.getHeaders().get("stripe-signature");
            var payload = request.getPayload();

            var event = Webhook.constructEvent(payload, signature, webhookSecret);

           return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                case "payment_intent.payment_failed" ->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

               default -> Optional.empty();
            };
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize stripe event. Check the SDK and API version.")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .putMetadata("order_id", item.getOrder().getId().toString())
                .build();
    }
}

package com.codewithmosh.store.feature.payments.api;

import com.codewithmosh.store.core.dto.ErrorDto;
import com.codewithmosh.store.feature.payments.core.dto.CheckoutRequestDto;
import com.codewithmosh.store.feature.carts.exception.CartEmptyException;
import com.codewithmosh.store.feature.carts.exception.CartNotFoundException;
import com.codewithmosh.store.feature.payments.core.exception.PaymentException;
import com.codewithmosh.store.feature.payments.core.service.CheckoutService;
import com.codewithmosh.store.feature.payments.core.model.WebHookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequestDto request) {
        return ResponseEntity.ok(checkoutService.checkout(request.getCartId()));
    }

    @PostMapping("checkout/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebHookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCartNotFoundException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}

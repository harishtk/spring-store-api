package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.CartDto;
import com.codewithmosh.store.dto.CartItemDto;
import com.codewithmosh.store.dto.ErrorDto;
import com.codewithmosh.store.dto.request.AddItemToCartRequest;
import com.codewithmosh.store.dto.request.UpdateCartItemRequestDto;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts", description = "Shopping cart management endpoints")
public class CartController {
    private final CartService cartService;

    /**
     * Retrieves all shopping carts in the system
     *
     * @return List of all shopping carts
     */
    @Operation(summary = "Get all shopping carts", description = "Retrieves a list of all shopping carts in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of carts")
    @GetMapping
    public ResponseEntity<List<CartDto>> getCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    /**
     * Creates a new shopping cart
     *
     * @param uriBuilder URI builder for location header
     * @return Newly created cart details
     */
    @Operation(summary = "Create new cart", description = "Creates a new empty shopping cart")
    @ApiResponse(responseCode = "201", description = "Cart successfully created")
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            @Parameter(hidden = true) UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    /**
     * Adds an item to an existing cart
     *
     * @param cartId  ID of the cart to add item to
     * @param request Details of the item to add
     * @return Added cart item details
     */
    @Operation(summary = "Add item to cart", description = "Adds a new item to an existing shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item successfully added to cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "ID of the cart") @PathVariable("id") UUID cartId,
            @Parameter(description = "Item details") @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    /**
     * Retrieves a specific cart by its ID
     *
     * @param cartId ID of the cart to retrieve
     * @return Cart details
     */
    @Operation(summary = "Get cart by ID", description = "Retrieves a specific shopping cart by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCartById(
            @Parameter(description = "ID of the cart") @PathVariable("id") UUID cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    /**
     * Updates quantity of an item in the cart
     *
     * @param cartId    ID of the cart
     * @param productId ID of the product to update
     * @param request   New quantity details
     * @return Updated cart item details
     */
    @Operation(summary = "Update cart item", description = "Updates the quantity of an item in the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item successfully updated"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @Parameter(description = "ID of the cart") @PathVariable("cartId") UUID cartId,
            @Parameter(description = "ID of the product") @PathVariable("productId") Long productId,
            @Parameter(description = "New quantity details") @Valid @RequestBody UpdateCartItemRequestDto request
    ) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    /**
     * Removes an item from the cart
     *
     * @param cartId    ID of the cart
     * @param productId ID of the product to remove
     * @return Empty response with status 204
     * @throws ProductNotFoundException if the product doesn't exist in the cart
     */
    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item successfully removed"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Product not found in cart")
    })
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(
            @Parameter(description = "ID of the cart") @PathVariable("cartId") UUID cartId,
            @Parameter(description = "ID of the product") @PathVariable("productId") Long productId
    ) throws ProductNotFoundException {
        cartService.removeProduct(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes all items from a cart
     *
     * @param cartId ID of the cart to clear
     * @return Empty response with status 204
     */
    @Operation(summary = "Clear cart", description = "Removes all items from the specified cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart successfully cleared"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @Parameter(description = "ID of the cart") @PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }
}

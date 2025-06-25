package com.codewithmosh.store.feature.carts.service;

import com.codewithmosh.store.feature.carts.dto.CartDto;
import com.codewithmosh.store.feature.carts.dto.CartItemDto;
import com.codewithmosh.store.feature.carts.entity.Cart;
import com.codewithmosh.store.feature.carts.exception.CartNotFoundException;
import com.codewithmosh.store.feature.inventory.exception.ProductNotFoundException;
import com.codewithmosh.store.feature.carts.mapper.CartMapper;
import com.codewithmosh.store.feature.carts.repository.CartRepository;
import com.codewithmosh.store.feature.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing shopping carts.
 * Provides operations for creating, retrieving, updating and managing cart items.
 */
@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    /**
     * Creates a new empty shopping cart.
     *
     * @return the created cart as DTO
     */
    public CartDto createCart() {
        var cart = cartRepository.save(new Cart());
        return cartMapper.toDto(cart);
    }

    /**
     * Retrieves all shopping carts with their items.
     *
     * @return list of all carts as DTOs
     */
    public List<CartDto> getAllCarts() {
        return cartRepository.getAllCartsWithItems().stream()
                .map(cartMapper::toDto)
                .toList();
    }

    /**
     * Adds a product to the specified cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product to add
     * @return the added cart item as DTO
     * @throws CartNotFoundException    if the cart is not found
     * @throws ProductNotFoundException if the product is not found
     */
    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = getCartOrThrow(cartId);
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        var cartItem = cart.addProduct(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    /**
     * Retrieves a cart by its ID.
     *
     * @param cartId the ID of the cart to retrieve
     * @return the cart as DTO
     * @throws CartNotFoundException if the cart is not found
     */
    public CartDto getCartById(UUID cartId) {
        return cartMapper.toDto(getCartOrThrow(cartId));
    }

    /**
     * Updates the quantity of a product in the specified cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product to update
     * @param quantity  the new quantity
     * @return the updated cart item as DTO
     * @throws CartNotFoundException    if the cart is not found
     * @throws ProductNotFoundException if the product is not found in the cart
     */
    public CartItemDto updateCartItem(UUID cartId, Long productId, int quantity) {
        var cart = getCartOrThrow(cartId);
        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException("Product not found");
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    /**
     * Removes a product from the specified cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product to remove
     * @throws CartNotFoundException if the cart is not found
     */
    public void removeProduct(UUID cartId, Long productId) {
        var cart = getCartOrThrow(cartId);
        cart.removeProduct(productId);
        cartRepository.save(cart);
    }

    /**
     * Removes all items from the specified cart.
     *
     * @param cartId the ID of the cart to clear
     * @throws CartNotFoundException if the cart is not found
     */
    public void clearCart(UUID cartId) {
        var cart = getCartOrThrow(cartId);
        cart.clear();
        cartRepository.save(cart);
    }

    /**
     * Retrieves a cart with its items or throws an exception if not found.
     *
     * @param cartId the ID of the cart to retrieve
     * @return the cart entity
     * @throws CartNotFoundException if the cart is not found
     */
    private Cart getCartOrThrow(UUID cartId) {
        return cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }
}

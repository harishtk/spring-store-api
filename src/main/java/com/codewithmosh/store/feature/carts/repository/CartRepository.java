package com.codewithmosh.store.feature.carts.repository;

import com.codewithmosh.store.feature.carts.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @EntityGraph(attributePaths = {"items.product"})
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);

    @EntityGraph(attributePaths = {"items.product"})
    @Query("SELECT c FROM Cart c")
    List<Cart> getAllCartsWithItems();
}

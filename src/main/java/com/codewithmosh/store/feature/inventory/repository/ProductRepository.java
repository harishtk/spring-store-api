package com.codewithmosh.store.feature.inventory.repository;

import com.codewithmosh.store.feature.inventory.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"category"})
    List<Product> findAllByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = {"category"})
    @Query("select p from Product p")
    List<Product> findAllWithCategory();
}
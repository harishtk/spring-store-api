package com.codewithmosh.store.feature.inventory.specs;

import com.codewithmosh.store.feature.inventory.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductEntitySpecifications {

    public static Specification<Product> hasCategoryId(Byte categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }
}

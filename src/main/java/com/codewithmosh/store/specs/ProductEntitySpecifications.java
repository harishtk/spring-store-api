package com.codewithmosh.store.specs;

import com.codewithmosh.store.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductEntitySpecifications {

    public static Specification<Product> hasCategoryId(Byte categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }
}

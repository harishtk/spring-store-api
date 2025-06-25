package com.codewithmosh.store.feature.inventory.repository;

import com.codewithmosh.store.feature.inventory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Byte> {
}
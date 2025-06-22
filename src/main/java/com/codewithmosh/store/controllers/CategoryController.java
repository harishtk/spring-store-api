package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.CategoryDto;
import com.codewithmosh.store.mappers.CategoryMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok().body(
                categoryRepository.findAll().stream().map(categoryMapper::toDto).toList()
        );
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            UriComponentsBuilder uriBuilder) {

        var category = categoryMapper.toEntity(categoryDto);
        category = categoryRepository.save(category);
        categoryDto.setId(category.getId());

        var uri = uriBuilder.path("/categories/{id}").buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Byte id,
            @Valid @RequestBody CategoryDto categoryDto) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        categoryMapper.update(categoryDto, category);
        categoryRepository.save(category);
        categoryDto.setId(category.getId());
        return ResponseEntity.ok().body(categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Byte id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

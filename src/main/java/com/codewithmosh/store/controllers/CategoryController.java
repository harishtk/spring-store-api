package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.CategoryDto;
import com.codewithmosh.store.mappers.CategoryMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * REST controller for managing categories.
 */
@Tag(name = "Category", description = "Category management APIs")
@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * GET /categories Get all categories
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categories in the body
     */
    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok().body(
                categoryRepository.findAll().stream().map(categoryMapper::toDto).toList()
        );
    }

    /**
     * POST /categories Create a new category
     *
     * @param categoryDto the category to create
     * @param uriBuilder  UriComponentsBuilder for creating location URI
     * @return the ResponseEntity with status 201 (Created) and with body the new category
     */
    @Operation(summary = "Create a new category")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
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

    /**
     * PUT /categories/:id Update an existing category
     *
     * @param id          the id of the category to update
     * @param categoryDto the category to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated category,
     * or with status 404 (Not Found) if the category is not found
     */
    @Operation(summary = "Update an existing category")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
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

    /**
     * DELETE /categories/:id Delete a category
     *
     * @param id the id of the category to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT),
     * or with status 404 (Not Found) if the category is not found
     */
    @Operation(summary = "Delete a category")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
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

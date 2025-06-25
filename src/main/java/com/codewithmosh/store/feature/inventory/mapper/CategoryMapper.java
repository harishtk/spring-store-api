package com.codewithmosh.store.feature.inventory.mapper;

import com.codewithmosh.store.feature.inventory.dto.CategoryDto;
import com.codewithmosh.store.feature.inventory.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void update(CategoryDto categoryDto, @MappingTarget Category category);
}

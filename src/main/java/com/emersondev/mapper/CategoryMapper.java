package com.emersondev.mapper;

import com.emersondev.api.request.CategoryRequest;
import com.emersondev.api.response.CategoryResponse;
import com.emersondev.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public Category toEntity(CategoryRequest request) {
    Category category = new Category();
    category.setName(request.getName());
    category.setDescription(request.getDescription());
    return category;
  }

  public void updateEntity(Category category, CategoryRequest request) {
    category.setName(request.getName());
    category.setDescription(request.getDescription());
  }

  public CategoryResponse toResponse(Category category) {
    return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .build();
  }
}

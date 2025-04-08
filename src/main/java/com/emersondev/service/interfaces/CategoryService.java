package com.emersondev.service.interfaces;

import com.emersondev.api.request.CategoryRequest;
import com.emersondev.api.response.CategoryResponse;
import com.emersondev.api.response.PagedResponse;

public interface CategoryService {

  CategoryResponse createCategory(CategoryRequest categoryRequest);
  CategoryResponse getCategoryById(Long id);
  CategoryResponse updateCategory(Long id ,CategoryRequest categoryRequest);

  PagedResponse<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir);

  void deleteCategory(Long id);
}

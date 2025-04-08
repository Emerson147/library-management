package com.emersondev.api.controller;

import com.emersondev.api.request.CategoryRequest;
import com.emersondev.api.response.CategoryResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.service.interfaces.CategoryService;
import com.emersondev.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<PagedResponse<CategoryResponse>> getAllCategories(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
          @RequestParam(defaultValue = "name") String sortBy,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

    PagedResponse<CategoryResponse> response = categoryService.getAllCategories(page, size, sortBy, sortDir);
    return ResponseEntity.ok(response);

  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
    CategoryResponse response = categoryService.createCategory(categoryRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
    CategoryResponse response = categoryService.getCategoryById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CategoryResponse> updateCategory(
          @PathVariable Long id,
          @Valid @RequestBody CategoryRequest categoryRequest) {
    CategoryResponse response = categoryService.updateCategory(id, categoryRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }

}

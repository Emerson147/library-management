package com.emersondev.service.impl;

import com.emersondev.api.request.CategoryRequest;
import com.emersondev.api.response.CategoryResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.domain.entity.Category;
import com.emersondev.domain.repository.BookRepository;
import com.emersondev.domain.repository.CategoryRepository;
import com.emersondev.mapper.CategoryMapper;
import com.emersondev.service.interfaces.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
  private final BookRepository bookRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository,
                             CategoryMapper categoryMapper, BookRepository bookRepository) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
    this.bookRepository = bookRepository;
  }

  @Override
  @Transactional
  public CategoryResponse createCategory(CategoryRequest categoryRequest) {
    Category category = categoryMapper.toEntity(categoryRequest);
    Category savedCategory = categoryRepository.save(category);
    return categoryMapper.toResponse(savedCategory);
  }

  @Override
  public PagedResponse<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Category> categories = categoryRepository.findAll(pageable);

    List<CategoryResponse> content = categories.getContent().stream()
            .map(categoryMapper::toResponse)
            .collect(Collectors.toList());

    return new PagedResponse<>(
            content,
            categories.getNumber(),
            categories.getSize(),
            categories.getTotalElements(),
            categories.getTotalPages(),
            categories.isLast());
  }

  @Override
  public CategoryResponse getCategoryById(Long id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

    return categoryMapper.toResponse(category);
  }

  @Override
  @Transactional
  public CategoryResponse updateCategory(Long id ,CategoryRequest categoryRequest) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

    category.setName(categoryRequest.getName());
    category.setDescription(categoryRequest.getDescription());

    Category updatedCategory = categoryRepository.save(category);
    return categoryMapper.toResponse(updatedCategory);
  }

  @Override
  public void deleteCategory(Long id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

//     Check if there are any active loans for this category
    long associateBooks = bookRepository.countByCategoryId(id);
    if (associateBooks > 0) {
      throw new RuntimeException("Cannot delete category with active associated books");
    }

    categoryRepository.delete(category);

  }
}

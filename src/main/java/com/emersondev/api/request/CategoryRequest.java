package com.emersondev.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

  @NotBlank(message = "Category name is required")
  private String name;
  @NotBlank(message = "Category description is required")
  private String description;
}

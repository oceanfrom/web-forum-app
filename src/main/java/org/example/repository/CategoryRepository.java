package org.example.repository;

import org.example.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);
}

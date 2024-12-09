package org.example.service;

import org.example.repository.CategoryRepository;
import org.example.model.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
}
package org.example.service.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.service.CategoryService;
import org.example.transaction.SessionManager;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    @Override
    public Category getCategoryById(Long id) {
        return SessionManager.executeReadOnly(session -> categoryRepository.getCategoryById(session, id));
    }

    @Override
    public List<Category> getAllCategories() {
        return SessionManager.executeReadOnly(session -> categoryRepository.getAllCategories(session));
    }
}

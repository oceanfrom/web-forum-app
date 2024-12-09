package org.example.repository.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.transaction.SessionManager;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public List<Category> getAllCategories() {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        });    }

    @Override
    public Category getCategoryById(Long id) {
        return SessionManager.executeReadOnly(session -> session.get(Category.class, id));
    }
}



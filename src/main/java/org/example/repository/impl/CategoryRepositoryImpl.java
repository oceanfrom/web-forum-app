package org.example.repository.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.hibernate.Session;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public List<Category> getAllCategories(Session session) {
        return session.createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    @Override
    public Category getCategoryById(Session session, Long id) {
        return session.get(Category.class, id);
    }
}



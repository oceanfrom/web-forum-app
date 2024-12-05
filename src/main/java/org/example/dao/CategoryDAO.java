package org.example.dao;

import org.example.model.Category;
import org.example.transaction.SessionManager;

import java.util.List;

public class CategoryDAO {
    public List<Category> getAllCategories() {
        return SessionManager.executeReadOnly(session -> {
           return session.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        });
    }

    public Category getCategoryById(Long id) {
        return SessionManager.executeReadOnly(session -> session.get(Category.class, id));
    }

}

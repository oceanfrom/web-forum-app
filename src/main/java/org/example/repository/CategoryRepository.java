package org.example.repository;

import org.example.model.Category;
import org.hibernate.Session;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAllCategories(Session session);

    Category getCategoryById(Session session, Long id);
}

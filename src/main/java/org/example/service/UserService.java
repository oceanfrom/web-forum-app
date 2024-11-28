package org.example.service;

import org.example.model.User;
import org.example.utils.impl.SessionManager;

public class UserService {
    public User getUserByUsername(String username) {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        });
    }
}

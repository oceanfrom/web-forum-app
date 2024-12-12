package org.example.factory;

import org.example.model.User;

public interface UserFactory {
    public User createUser(String username, String email, String password);
}

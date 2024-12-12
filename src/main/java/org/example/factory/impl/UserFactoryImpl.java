package org.example.factory.impl;

import org.example.factory.UserFactory;
import org.example.model.User;

public class UserFactoryImpl implements UserFactory {
    @Override
    public User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}

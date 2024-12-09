package org.example.service;

import org.example.model.User;

public interface AuthService {
    String authenticateUser(String email, String password);
    String registerUser(User user);
}

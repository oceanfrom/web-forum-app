package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.model.User;
import org.example.model.enumiration.Role;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.example.utils.PasswordEncoder;

public class AuthServiceImpl implements AuthService {
    private final UserService userService = new UserServiceImpl();

    @Override
    public String authenticateUser(String email, String password) {
        User user = userService.getUserByEmail(email);
        if (user == null || !PasswordEncoder.checkPassword(password, user.getPassword())) {
            return "Invalid email or password";
        }
        return null;
    }

    @Transactional
    @Override
    public String registerUser(User user) {
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return "Email already in use.";
        }
        String hashedPassword = PasswordEncoder.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        userService.saveUser(user);
        return null;
    }

}

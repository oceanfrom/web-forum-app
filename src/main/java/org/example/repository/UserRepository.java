package org.example.repository;

import org.example.model.User;

import java.util.List;

public interface UserRepository {
    List<User> searchByName(String username);

    void deleteUserById(Long userId);

    void saveUser(User user);

    User getUserByEmail(String email);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);
}

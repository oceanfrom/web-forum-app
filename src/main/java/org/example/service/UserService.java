package org.example.service;

import org.example.model.User;
import org.hibernate.Session;

import java.util.List;

public interface UserService {
    List<User> searchByName(String username);

    void deleteUserById(Long userId);

    void saveUser(Session session, User user);

    User getUserByEmail(Session session, String email);

    User getUserByEmail(String email);

    User getUserById(Long id);

    List<User> getAllUsers();

    String updateUsername(User currentUser, String newUsername);

    String updatePassword(User currentUser, String newPassword);

    String updateEmail(User currentUser, String newEmail);
}

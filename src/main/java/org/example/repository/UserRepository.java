package org.example.repository;

import org.example.model.User;
import org.hibernate.Session;

import java.util.List;

public interface UserRepository {
    List<User> searchByName(Session session, String username);

    void deleteUserById(Session session, Long userId);

    void saveUser(Session session, User user);

    User getUserByEmail(Session session, String email);

    void updateUser(Session session, User user);

    List<User> getAllUsers(Session session);

    User getUserById(Session session, Long id);
}

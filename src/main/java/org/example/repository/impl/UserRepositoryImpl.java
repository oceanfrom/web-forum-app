package org.example.repository.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.transaction.SessionManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void deleteUserById(Long userId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            User user = session.get(User.class, userId);
            if (user == null)
                return;
            session.delete(user);
        });
    }

    @Override
    public void saveUser(User user) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.save(user));
    }

    @Override
    public User getUserByEmail(String email) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        });
    }

    @Override
    public void updateUser(User user) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.update(user));
    }

    @Override
    public List<User> getAllUsers() {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        });
    }

    @Override
    public User getUserById(Long id) {
        return SessionManager.executeReadOnly(session -> session.get(User.class, id));
    }
}

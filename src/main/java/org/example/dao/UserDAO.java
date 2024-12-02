package org.example.dao;

import org.example.model.User;
import org.example.transaction.SessionManager;
import java.util.List;

public class UserDAO {
    public User getUserByEmail(String email) {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        });
    }

    public void updateUser(User user) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.update(user));
    }

    public List<User> getAllUsers() {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        });
    }


    public User getUserById(Long id) {
       return SessionManager.executeInTransaction(session -> session.get(User.class, id));
    }
}

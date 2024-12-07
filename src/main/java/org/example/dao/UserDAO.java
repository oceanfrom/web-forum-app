package org.example.dao;

import org.example.model.Topic;
import org.example.model.User;
import org.example.transaction.SessionManager;
import java.util.List;

public class UserDAO {

    public void deleteUserById(Long userId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            User user = session.get(User.class, userId);
            if (user == null)
                return;
            session.delete(user);
        });
    }

    public void saveUser(User user) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.save(user));
    }

    public User getUserByEmail(String email) {
        return SessionManager.executeReadOnly(session -> {
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
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        });
    }


    public User getUserById(Long id) {
       return SessionManager.executeReadOnly(session -> session.get(User.class, id));
    }
}

package org.example.repository.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.hibernate.Session;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public List<User> searchByName(Session session, String username) {
        return session.createQuery("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(:username) ", User.class)
                .setParameter("username", "%" + username + "%")

                .getResultList();
    }

    @Override
    public void deleteUserById(Session session, Long userId) {
        User user = session.get(User.class, userId);
        if (user == null)
            return;
        session.delete(user);
    }

    @Override
    public void saveUser(Session session, User user) {
        session.save(user);
    }

    @Override
    public User getUserByEmail(Session session, String email) {
        return session.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public void updateUser(Session session, User user) {
        session.update(user);
    }

    @Override
    public List<User> getAllUsers(Session session) {
        return session.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUserById(Session session, Long id) {
        return session.get(User.class, id);
    }
}

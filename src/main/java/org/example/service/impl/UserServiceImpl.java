package org.example.service.impl;

import jakarta.validation.*;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.UserService;
import org.example.transaction.SessionManager;
import org.example.utils.PasswordEncoder;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Override
    public List<User> searchByName(String username) {
        return SessionManager.executeReadOnly(session -> userRepository.searchByName(session, username));
    }

    @Override
    public void deleteUserById(Long userId) {
        SessionManager.executeInTransactionWithoutReturn(session -> userRepository.deleteUserById(session, userId));
    }

    @Override
    public void saveUser(Session session, User user) {
        userRepository.saveUser(session, user);
    }

    @Override
    public User getUserByEmail(Session session, String email) {
        return userRepository.getUserByEmail(session, email);
    }

    @Override
    public User getUserByEmail(String email) {
        return SessionManager.executeReadOnly(session -> userRepository.getUserByEmail(session, email));
    }

    @Override
    public User getUserById(Long id) {
        return SessionManager.executeReadOnly(session -> userRepository.getUserById(session, id));
    }

    @Override
    public List<User> getAllUsers() {
        return SessionManager.executeReadOnly(userRepository::getAllUsers);
    }

    private String validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

    @Override
    public String updateUsername(User currentUser, String newUsername) {
        return SessionManager.executeInTransaction(session -> {
            currentUser.setUsername(newUsername);
            String validationError = validateUser(currentUser);
            if (validationError != null) {
                return validationError;
            }
            userRepository.updateUser(session, currentUser);
            return null;
        });
    }

    @Override
    public String updatePassword(User currentUser, String newPassword) {
        return SessionManager.executeInTransaction(session -> {
            String hashedPassword = PasswordEncoder.hashPassword(newPassword);
            currentUser.setPassword(hashedPassword);

            String validationError = validateUser(currentUser);
            if (validationError != null) {
                return validationError;
            }
            userRepository.updateUser(session, currentUser);
            return null;
        });
    }

    @Override
    public String updateEmail(User currentUser, String newEmail) {
       return SessionManager.executeInTransaction(session -> {
            if (!isEmailUnique(session, currentUser, newEmail)) {
                return "Email address already in use";
            }
            currentUser.setEmail(newEmail);
            String validationError = validateUser(currentUser);
            if (validationError != null) {
                return validationError;
            }
            userRepository.updateUser(session, currentUser);
            return null;
        });
    }

    private boolean isEmailUnique(Session session, User currentUser, String email) {
        User userWithEmail = userRepository.getUserByEmail(session, email);
        return userWithEmail == null || userWithEmail.getId().equals(currentUser.getId());
    }
}

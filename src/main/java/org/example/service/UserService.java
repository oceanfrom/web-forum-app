package org.example.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.dao.UserDAO;
import org.example.model.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();;

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
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

    public String authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return "Invalid email or password";
        }
        return null;
    }

    public String updateUsername(User currentUser, String newUsername) {
        currentUser.setUsername(newUsername);
        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userDAO.updateUser(currentUser);
        return null;
    }

    public String updatePassword(User currentUser, String newPassword) {
        currentUser.setPassword(newPassword);
        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userDAO.updateUser(currentUser);
        return null;
    }

    @Transactional
    public String updateEmail(User currentUser, String newEmail) {
        if (!isEmailUnique(currentUser, newEmail)) {
            return "Email address already in use";
        }
        currentUser.setEmail(newEmail);
        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userDAO.updateUser(currentUser);
        return null;
    }

    private boolean isEmailUnique(User currentUser, String email) {
        User userWithEmail = userDAO.getUserByEmail(email);
        return userWithEmail == null || userWithEmail.getId().equals(currentUser.getId());
    }

}

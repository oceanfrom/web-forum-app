package org.example.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.*;
import org.example.model.User;
import org.example.model.enumiration.Role;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.UserService;
import org.example.utils.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
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
        currentUser.setUsername(newUsername);
        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userRepository.updateUser(currentUser);
        return null;
    }

    @Override
    public String updatePassword(User currentUser, String newPassword) {
        String hashedPassword = PasswordEncoder.hashPassword(newPassword);
        currentUser.setPassword(hashedPassword);

        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userRepository.updateUser(currentUser);
        return null;
    }

    @Transactional
    @Override
    public String updateEmail(User currentUser, String newEmail) {
        if (!isEmailUnique(currentUser, newEmail)) {
            return "Email address already in use";
        }
        currentUser.setEmail(newEmail);
        String validationError = validateUser(currentUser);
        if (validationError != null) {
            return validationError;
        }
        userRepository.updateUser(currentUser);
        return null;
    }

    private boolean isEmailUnique(User currentUser, String email) {
        User userWithEmail = userRepository.getUserByEmail(email);
        return userWithEmail == null || userWithEmail.getId().equals(currentUser.getId());
    }
}

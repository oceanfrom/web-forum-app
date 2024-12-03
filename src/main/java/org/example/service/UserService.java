package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.validator.EmailValidator;
import org.example.validator.NameValidator;
import org.example.validator.PasswordValidator;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    private EmailValidator emailValidator = new EmailValidator();
    private NameValidator nameValidator = new NameValidator();
    private PasswordValidator passwordValidator = new PasswordValidator();

    public String authenticateUser(String email, String password) {
        if (emailValidator.isValidEmail(email))
            return "Invalid email";

        if (passwordValidator.isValidPassword(password))
            return "Password must be between 6 and 15 characters";

        User user = userDAO.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password))
            return "Invalid email or password";

        return null;
    }

    public String updateUsername(User currentUser, String newUsername) {
        if (!nameValidator.isValidName(newUsername))
            return "Invalid username";

        currentUser.setUsername(newUsername);
        userDAO.updateUser(currentUser);
        return null;
    }

    public String updatePassword(User currentUser, String newPassword) {
        if (passwordValidator.isValidPassword(newPassword))
            return "Password must be between 6 and 15 characters";

        currentUser.setPassword(newPassword);
        userDAO.updateUser(currentUser);
        return null;
    }

    public String updateEmail(User currentUser, String newEmail) {
        if (emailValidator.isValidEmail(newEmail))
            return "Not correct email address";
        else if (emailValidator.isEmailTaken(newEmail))
            return "Email address already in use";

        currentUser.setEmail(newEmail);
        userDAO.updateUser(currentUser);
        return null;
    }

}

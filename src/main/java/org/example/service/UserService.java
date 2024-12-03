package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.validator.EmailValidator;
import org.example.validator.NameValidator;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User getCurrentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    public Boolean isLoggedIn(HttpServletRequest req) {
        return (Boolean) req.getSession().getAttribute("loggedIn");
    }

    public boolean isEmailTaken(String email) {
        User user = userDAO.getUserByEmail(email);
        return user != null;
    }

    public String authenticateUser(String email, String password) {
        if (!EmailValidator.isValidEmail(email))
            return "Invalid email";

        if (password == null || password.length() < 6)
            return "Password must be at least 6 characters";

        User user = userDAO.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password))
            return "Invalid email or password";

        return null;
    }

    public String updateUsername(User currentUser, String newUsername) {
        if (!NameValidator.isValidName(newUsername))
            return "Invalid username";

        currentUser.setUsername(newUsername);
        userDAO.updateUser(currentUser);
        return null;
    }

    public String updatePassword(User currentUser, String newPassword) {
        if (newPassword == null || newPassword.length() < 6)
            return "Password must be at least 6 characters";

        currentUser.setPassword(newPassword);
        userDAO.updateUser(currentUser);
        return null;
    }

    public String updateEmail(User currentUser, String newEmail) {
        if (!EmailValidator.isValidEmail(newEmail))
            return "Not correct email address";
        else if (isEmailTaken(newEmail))
            return "Email address already in use";

        currentUser.setEmail(newEmail);
        userDAO.updateUser(currentUser);
        return null;
    }

}

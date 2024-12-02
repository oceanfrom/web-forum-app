package service;

import org.example.dao.UserDAO;
import org.example.model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean isEmailTaken(String email) {
        User user = userDAO.getUserByEmail(email);
        return user != null;
    }
}

package org.example.validator;

import org.example.dao.UserDAO;
import org.example.model.User;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class EmailValidator {
    private UserDAO userDAO = new UserDAO();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())
            return true;

        String domain = email.substring(email.indexOf('@') + 1);
        return !isDomainExist(domain);
    }

    private boolean isDomainExist(String domain) {
        try {
            InetAddress inet = InetAddress.getByName(domain);
            return inet != null;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public boolean isEmailTaken(String email) {
        User user = userDAO.getUserByEmail(email);
        return user != null;
    }

}

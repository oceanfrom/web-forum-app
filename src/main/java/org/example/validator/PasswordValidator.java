package org.example.validator;

public class PasswordValidator {
    public boolean isValidPassword(String password) {
        return password == null || password.length() < 6 || password.length() > 15;
    }
}

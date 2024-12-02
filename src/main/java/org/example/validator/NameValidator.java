package org.example.validator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NameValidator {
    private static final String NAME_REGEX = "^[A-Z][a-zA-Z]* [A-Z][a-zA-Z]*$";

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}

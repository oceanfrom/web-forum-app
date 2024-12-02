package org.example.validator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())
            return false;

        String domain = email.substring(email.indexOf('@') + 1);
        return isDomainExist(domain);
    }

    public static boolean isDomainExist(String domain) {
        try {
            InetAddress inet = InetAddress.getByName(domain);
            return inet != null;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}

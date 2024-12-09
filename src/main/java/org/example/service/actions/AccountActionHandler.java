package org.example.service.actions;

import jakarta.servlet.http.HttpServletRequest;
import org.example.model.User;

import java.io.IOException;

public interface AccountActionHandler {
    String execute(HttpServletRequest req, User currentUser) throws IOException;
}

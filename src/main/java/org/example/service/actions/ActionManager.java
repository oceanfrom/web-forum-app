package org.example.service.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;

import java.io.IOException;

public interface ActionManager {
    void handleAction(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException;

}

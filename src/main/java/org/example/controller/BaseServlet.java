package org.example.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.utils.IdParserUtils;

import java.io.IOException;

@Slf4j
public abstract class BaseServlet extends HttpServlet {

    protected User getCurrentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    protected boolean isUserLoggedIn(HttpServletRequest req) {
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        return loggedIn != null && loggedIn;
    }

    protected void ensureUserLoggedIn(HttpServletResponse resp, User currentUser) throws IOException {
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            throw new IOException();
        }
    }

    protected void redirectIfUserLoggedIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser != null) {
            log.info("User already logged in, redirecting to forum.");
            resp.sendRedirect("/forum");
            throw new IOException();
        }
    }

    protected Long parseIdWithSubstring(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long entityId = null;
        try {
            entityId = IdParserUtils.parseIdWithSubstring(req.getPathInfo());
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing {} ID", entityId, e);
            handleError(resp);
        }
        return entityId;
    }

    protected Long parseId(HttpServletResponse resp, String id) throws IOException {
        Long entityId = null;
        try {
            entityId = IdParserUtils.parseId(id);
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing {} ID", entityId, e);
            handleError(resp);
        }
        return entityId;
    }

    protected void handleError(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/error");
    }

    protected void handleRedirect(HttpServletResponse resp, String redirectUrl) throws IOException {
        resp.sendRedirect(redirectUrl);
    }


}

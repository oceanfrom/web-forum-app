package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.dao.UserDAO;
import org.example.config.ThymeleafConfig;
import org.example.service.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserDAO userDAO;
    private UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userDAO = new UserDAO();
        userService = new UserService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            log.info("User already logged in, redirecting to forum.");
            resp.sendRedirect("/forum");
        }

        Context context = new Context();
        templateEngine.process("login", context, resp.getWriter());
        log.info("Login page rendered successfully.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String errorMessage = userService.authenticateUser(email, password);
        if (errorMessage != null) {
            log.warn("Authentication failed: {}", errorMessage);
            resp.sendRedirect("/login?error=" + errorMessage);
            return;
        }

        User user = userDAO.getUserByEmail(email);
        req.getSession().setAttribute("user", user);
        req.getSession().setAttribute("loggedIn", true);
        log.info("User {} logged in successfully.", email);
        resp.sendRedirect("/forum");
    }
}

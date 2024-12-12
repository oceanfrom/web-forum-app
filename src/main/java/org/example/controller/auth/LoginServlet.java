package org.example.controller.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.example.service.impl.AuthServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService;
    private AuthService authService;
    private ContextGeneration contextGeneration;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserServiceImpl();
        authService = new AuthServiceImpl();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser != null) {
            log.info("User already logged in, redirecting to forum.");
            resp.sendRedirect("/forum");
            return;
        }
        Context context = new Context();
        templateEngine.process("login", context, resp.getWriter());
        log.info("Login page rendered successfully.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String errorMessage = authService.authenticateUser(email, password);
        if (errorMessage != null) {
            log.warn("Authentication failed: {}", errorMessage);
            Context context = contextGeneration.createContext("errorMessage",errorMessage);
            templateEngine.process("login", context, resp.getWriter());
            return;
        }

        User user = userService.getUserByEmail(email);
        req.getSession().setAttribute("user", user);
        req.getSession().setAttribute("loggedIn", true);
        log.info("User {} logged in successfully.", email);
        resp.sendRedirect("/forum");
    }
}

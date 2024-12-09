package org.example.controller.auth;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ThymeleafConfig;
import org.example.factory.UserFactory;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.impl.AuthServiceImpl;
import org.example.controller.BaseServlet;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Slf4j
@WebServlet("/register")
public class RegisterServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private AuthService authService;
    private UserFactory userFactory;
    private ContextGeneration contextGeneration;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        authService = new AuthServiceImpl();
        userFactory = new UserFactory();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        templateEngine.process("register", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userFactory.createUser(username, email, password);
        String errorMessage = authService.registerUser(user);

        if (errorMessage != null) {
            log.warn("Registration failed: {}", errorMessage);
            Context context = contextGeneration.createContext("errorMessage",errorMessage);
            templateEngine.process("register", context, resp.getWriter());
            return;
        }
        log.info("User {} registered successfully.", email);
        handleRedirect(resp, "/login");

    }

}

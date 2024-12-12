package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService;
    private ContextGeneration contextGeneration;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserServiceImpl();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchQuery = req.getParameter("search");
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
                List<User> users;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            users = userService.searchByName(searchQuery);
            log.info("Users: {}; ", users);
        } else {
            users = userService.getAllUsers();
        }
        Context context = contextGeneration.createContext("users", users, "user", currentUser);
        templateEngine.process("users", context, resp.getWriter());
        log.info("Successfully rendered users page for user: '{}'.", currentUser.getUsername());
    }
}

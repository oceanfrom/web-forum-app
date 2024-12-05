package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.UserService;
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

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        List<User> users = userService.getAllUsers();

        Context context = createContextVal(users, currentUser);
        templateEngine.process("users", context, resp.getWriter());
        log.info("Successfully rendered users page for user: '{}'.", currentUser.getUsername());
    }

    public Context createContextVal(List<User> users, User currentUser) {
        Context context = new Context();
        context.setVariable("users", users);
        context.setVariable("user", currentUser);
        return context;
    }
}

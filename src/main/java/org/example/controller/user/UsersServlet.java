package org.example.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.controller.BaseServlet;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet("/users")
public class UsersServlet extends BaseServlet {
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
        User currentUser = getCurrentUser(req);
        try {
            ensureUserLoggedIn(resp,currentUser);
        } catch (IOException e) {
            return;
        }
        List<User> users = userService.getAllUsers();
        Context context = contextGeneration.createContext("users", users, "user", currentUser);
        templateEngine.process("users", context, resp.getWriter());
        log.info("Successfully rendered users page for user: '{}'.", currentUser.getUsername());
    }

}

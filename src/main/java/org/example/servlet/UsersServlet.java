package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.UserDAO;
import org.example.config.ThymeleafConfig;
import org.example.service.UserService;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet("/users")
public class UsersServlet extends HttpServlet {
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
        User currentUser = userService.getCurrentUser(req);
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        List<User> users = userDAO.getAllUsers();

        Map<String, Object> contextVal = new HashMap<>();
        contextVal.put("users", users != null ? users : new ArrayList<>());
        contextVal.put("currentUser", currentUser);
        Context context = ThymeleafContextUtils.createContext(contextVal);
        templateEngine.process("users", context, resp.getWriter());
        log.info("Successfully rendered users page for user: '{}'.", currentUser.getUsername());
    }
}

package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.TopicDAO;
import org.example.config.ThymeleafConfig;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.example.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/account/*")
public class AccountServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicDAO topicDAO;
    private UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicDAO = new TopicDAO();
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
        List<Topic> createdTopics = topicDAO.getCreatedTopicsByUser(currentUser.getId());
        List<Topic> likedTopics = topicDAO.getLikedTopicsByUser(currentUser.getId());

        Map<String, Object> contextVal = new HashMap<>();
        contextVal.put("createdTopics", createdTopics != null ? createdTopics : new ArrayList<>());
        contextVal.put("likedTopics", likedTopics != null ? likedTopics : new ArrayList<>());
        contextVal.put("user", currentUser);
        Context context = ThymeleafContextUtils.createContext(contextVal);
        templateEngine.process("account", context, resp.getWriter());
        log.info("Successfully rendered account page for user {}", currentUser.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getRequestURI();
        String errorMessage = "";
        User currentUser = userService.getCurrentUser(req);

        switch (action) {
            case "/account/update-username":
                errorMessage = userService.updateUsername(currentUser, req.getParameter("username"));
                break;

            case "/account/update-password":
                errorMessage = userService.updatePassword(currentUser, req.getParameter("password"));
                break;

            case "/account/update-email":
                errorMessage = userService.updateEmail(currentUser, req.getParameter("email"));
                break;

            default:
                log.warn("Unknown action: {}", action);
                break;
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            log.warn("Error updating account settings: {}", errorMessage);
            Map<String, Object> contextVal = new HashMap<>();
            contextVal.put("user", currentUser);
            contextVal.put("errorMessage", errorMessage);
            Context context = ThymeleafContextUtils.createContext(contextVal);
            templateEngine.process("account", context, resp.getWriter());
        } else {
            log.info("Changes saved successfully for user {}", currentUser.getId());
            resp.sendRedirect("/account");
        }
    }
}

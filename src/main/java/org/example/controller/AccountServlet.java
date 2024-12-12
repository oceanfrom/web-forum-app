package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.TopicService;
import org.example.service.impl.TopicServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.example.service.UserService;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/account/*")
public class AccountServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private UserService userService;
    private ContextGeneration contextGeneration;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicServiceImpl();
        userService = new UserServiceImpl();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        List<Topic> createdTopics = topicService.getCreatedTopicsByUser(currentUser.getId());
        List<Topic> likedTopics = topicService.getLikedTopicsByUser(currentUser.getId());

        Context context = contextGeneration.createContext("createdTopics", createdTopics, "likedTopics",
                likedTopics, "user", currentUser);
        templateEngine.process("account", context, resp.getWriter());
        log.info("Successfully rendered account page for user {}", currentUser.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String errorMessage = "";
        User currentUser = (User) req.getSession().getAttribute("user");
        switch (action) {
            case "update-username":
                errorMessage = userService.updateUsername(currentUser, req.getParameter("username"));
                break;

            case "update-password":
                errorMessage = userService.updatePassword(currentUser, req.getParameter("password"));
                break;

            case "update-email":
                errorMessage = userService.updateEmail(currentUser, req.getParameter("email"));
                break;
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            log.warn("Error updating account settings: {}", errorMessage);
            Context context = contextGeneration.createContext("user", currentUser, "errorMessage", errorMessage);
            templateEngine.process("account", context, resp.getWriter());
        } else {
            log.info("Changes saved successfully for user {}", currentUser.getId());
            resp.sendRedirect("/account");
        }
    }
}

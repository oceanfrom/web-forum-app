package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.TopicDAO;
import org.example.config.ThymeleafConfig;
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
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        List<Topic> createdTopics = topicDAO.getCreatedTopicsByUser(currentUser.getId());
        List<Topic> likedTopics = topicDAO.getLikedTopicsByUser(currentUser.getId());

        Context context = createContextVal(createdTopics, likedTopics, currentUser);
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
            Context context = createContextErrorVal(currentUser, errorMessage);
            templateEngine.process("account", context, resp.getWriter());
        } else {
            log.info("Changes saved successfully for user {}", currentUser.getId());
            resp.sendRedirect("/account");
        }
    }

    private Context createContextVal(List<Topic> createdTopics, List<Topic> likedTopics, User currentUser ) {
        Context context = new Context();
        context.setVariable("createdTopics", createdTopics);
        context.setVariable("likedTopics", likedTopics);
        context.setVariable("user", currentUser);
        return context;
    }

    private Context createContextErrorVal(User currentUser, String errorMessage) {
        Context context = new Context();
        context.setVariable("user", currentUser);
        context.setVariable("errorMessage", errorMessage);
        return context;
    }
}

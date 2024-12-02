package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.TopicDAO;
import org.example.dao.UserDAO;
import org.example.config.ThymeleafConfig;
import org.example.validator.EmailValidator;
import org.example.validator.NameValidator;
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
    private UserDAO userDAO;
    private TopicDAO topicDAO;
    private UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userDAO = new UserDAO();
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

        Context context = new Context();
        context.setVariable("createdTopics", createdTopics);
        context.setVariable("likedTopics", likedTopics);
        context.setVariable("user", currentUser);
        templateEngine.process("account", context, resp.getWriter());
        log.info("Successfully rendered account page for user {}", currentUser.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getRequestURI();
        String errorMessage = "";
        User currentUser = (User) req.getSession().getAttribute("user");

        if (action.equals("/account/update-username")) {
            String newUsername = req.getParameter("username");
            if (!NameValidator.isValidName(newUsername)) {
                errorMessage = "Invalid username";
                log.warn("Invalid username: {}", newUsername);
            } else {
                currentUser.setUsername(newUsername);
                userDAO.updateUser(currentUser);
                log.info("Username updated successfully for user {}", currentUser.getId());
            }
        } else if (action.equals("/account/update-password")) {
            String newPassword = req.getParameter("password");
            if (newPassword == null || newPassword.length() < 6) {
                errorMessage = "Password must be at least 6 characters";
                log.warn("Invalid password: length less than 6 characters.");
            } else {
                currentUser.setPassword(newPassword);
                userDAO.updateUser(currentUser);
                log.info("Password updated successfully for user {}", currentUser.getId());
            }
        } else if (action.equals("/account/update-email")) {
            String newEmail = req.getParameter("email");
            if (!EmailValidator.isValidEmail(newEmail)) {
                errorMessage = "Not correct email address";
                log.warn("Invalid email format: {}", newEmail);
            } else if (userService.isEmailTaken(newEmail)) {
                errorMessage = "Email address already in use";
                log.warn("Email address already taken: {}", newEmail);
            } else {
                currentUser.setEmail(newEmail);
                userDAO.updateUser(currentUser);
                log.info("Email updated successfully for user {}", currentUser.getId());
            }
        }

        if (!errorMessage.isEmpty()) {
            log.warn("Error updating account settings: {}", errorMessage);
            Context context = new Context();
            context.setVariable("user", currentUser);
            context.setVariable("errorMessage", errorMessage);
            templateEngine.process("account", context, resp.getWriter());
        } else {
            log.info("Changes saved successfully for user {}", currentUser.getId());
            resp.sendRedirect("/account");
        }
    }
}

package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.TopicService;
import org.example.service.UserService;
import org.example.utils.IdParserUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService;
    private TopicService topicService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserService();
        topicService = new TopicService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        Long userId = null;
        try {
            userId = IdParserUtils.parseIdWithSubstring(req.getPathInfo());
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            resp.sendRedirect(req.getContextPath() + "/error");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            log.warn("User not found with ID: {}", userId);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        if (currentUser.getId().equals(user.getId())) {
            log.info("Current user is the same as the profile user, redirecting to /account");
            resp.sendRedirect(req.getContextPath() + "/account");
            return;
        }
        List<Topic> createdTopics = topicService.getCreatedTopicsByUser(user.getId());

        Context context = createContextVal(currentUser, user, createdTopics);
        templateEngine.process("profile", context, resp.getWriter());
        log.info("Successfully rendered profile page for user {}", userId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            handleDeleteUser(req, resp);
        } catch (IdParserUtils.InvalidIdException e) {
            resp.sendRedirect(req.getContextPath() + "/error");
        }
    }

    private void handleDeleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, IdParserUtils.InvalidIdException {
        User currentUser = (User) req.getSession().getAttribute("user");
        Long userId = IdParserUtils.parseId(req.getParameter("userId"));
        if (currentUser != null && "admin".equals(currentUser.getRole().toString().toLowerCase())) {
            userService.deleteUserById(userId);
            resp.sendRedirect(req.getContextPath() + "/users");
        } else {
            log.warn("Unauthorized attempt to delete user.");
            resp.sendRedirect(req.getContextPath() + "/error");
        }
    }

        private Context createContextVal(User currentUser, User user, List<Topic> createdTopics) {
        Context context = new Context();
        context.setVariable("currentUser", currentUser);
        context.setVariable("user", user);
        context.setVariable("createdTopics", createdTopics);
        return context;
    }

}

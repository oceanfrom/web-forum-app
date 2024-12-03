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
import org.example.service.UserService;
import org.example.utils.IdParserUtils;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {
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
        User currentUser = userService.getCurrentUser(req);
        if (currentUser == null) {
            log.warn("User not logged in, redirecting to login page.");
            resp.sendRedirect("/login");
            return;
        }
        Long userId = IdParserUtils.parseId(req.getPathInfo(), req, resp);
        User user = userDAO.getUserById(userId);
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
        List<Topic> createdTopics = topicDAO.getCreatedTopicsByUser(user.getId());

        Map<String, Object> contextVal = new HashMap<>();
        contextVal.put("user", user);
        contextVal.put("createdTopics", createdTopics != null ? createdTopics : new ArrayList<>());
        Context context = ThymeleafContextUtils.createContext(contextVal);
        templateEngine.process("profile", context, resp.getWriter());
        log.info("Successfully rendered profile page for user {}", userId);
    }

}

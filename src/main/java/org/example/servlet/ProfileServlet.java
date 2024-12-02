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
    private UserDAO userDAO;
    private TopicDAO topicDAO;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userDAO = new UserDAO();
        topicDAO = new TopicDAO();
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
            userId = IdParserUtils.parseId(req.getPathInfo());
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            resp.sendRedirect(req.getContextPath() + "/error");
        }
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

        Context context = createContextVal(user, createdTopics);
        templateEngine.process("profile", context, resp.getWriter());
        log.info("Successfully rendered profile page for user {}", userId);
    }

    private Context createContextVal(User user, List<Topic> createdTopics) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("createdTopics", createdTopics);
        return context;
    }

}

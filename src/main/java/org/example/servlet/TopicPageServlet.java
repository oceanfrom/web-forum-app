package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.CategoryDAO;
import org.example.model.Category;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.CommentDAO;
import org.example.dao.TopicDAO;
import org.example.config.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import service.TopicService;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/topic/*")
public class TopicPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicDAO topicDAO;
    private TopicService topicService;
    private CommentDAO commentDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicDAO = new TopicDAO();
        commentDAO = new CommentDAO();
        topicService = new TopicService();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty()) {
            log.warn("Invalid path, redirecting to error page.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Long topicId;
        try {
            topicId = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid topicId in path: {}", path, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }

        User currentUser = (User) req.getSession().getAttribute("user");
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        log.debug("Current user: {}, Logged in: {}", currentUser, loggedIn);

        Topic topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            log.warn("Topic with id {} not found, redirecting to error page.", topicId);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }

        List<Comment> comments = commentDAO.getCommentsByTopicId(topicId);
        log.debug("Loaded {} comments for topicId {}", comments.size(), topicId);

        User topicCreator = topic.getCreatedBy();

        Context context = new Context();
        context.setVariable("topic", topic);
        context.setVariable("loggedIn", loggedIn);
        context.setVariable("currentUser", currentUser);
        context.setVariable("topicCreator", topicCreator);
        context.setVariable("comments", comments);
        resp.setContentType("text/html");
        templateEngine.process("topic", context, resp.getWriter());
        log.info("Successfully processed GET request for topicId {}", topicId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = (User) req.getSession().getAttribute("user");
        if ("create".equals(action)) {
            String title = req.getParameter("title");
            String categoryIdStr = req.getParameter("categoryIdStr");
            String description = req.getParameter("description");

            if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                log.warn("Missing or invalid input: title, description, or category ID is empty.");
                resp.sendRedirect(req.getContextPath() + "/error");
                return;
            }
            Long categoryId;
            try {
                categoryId = Long.parseLong(categoryIdStr.trim());
            } catch (NumberFormatException e) {
                log.error("Invalid category ID format: {}", categoryIdStr, e);
                resp.sendRedirect(req.getContextPath() + "/error");
                return;
            }

            Category category = categoryDAO.getCategoryById(categoryId);
            topicDAO.addTopic(title, description, categoryId, currentUser);
            log.info("Successfully created topic with title: '{}' in category: '{}'.", title, category.getName());
            resp.sendRedirect(req.getContextPath() + "/forum");
            return;
        }

        String topicIdString = req.getParameter("topicId");
        if (topicIdString == null || topicIdString.isEmpty()) {
            log.warn("topicId is null or empty, redirecting to error page.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        Long topicId;
        try {
            topicId = Long.parseLong(topicIdString);
        } catch (NumberFormatException e) {
            log.error("Invalid topic ID format: {}", topicIdString, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }

        if ("like".equals(action)) {
            log.info("User {} liked topic {}", currentUser, topicId);
            topicService.updateRating(topicId, currentUser, true);
        } else if ("dislike".equals(action)) {
            log.info("User {} disliked topic {}", currentUser, topicId);
            topicService.updateRating(topicId, currentUser, false);
        } else if ("deleteTopic".equals(action)) {
            topicDAO.deleteTopicById(topicId);
            log.info("User {} deleted topic {}", currentUser, topicId);
            resp.sendRedirect(req.getContextPath() + "/forum");
            return;
        }
        resp.sendRedirect(req.getRequestURI());
        log.info("Successfully processed POST request for topicId {}", topicId);
    }

}

package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.CategoryDAO;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.CommentDAO;
import org.example.dao.TopicDAO;
import org.example.config.ThymeleafConfig;
import org.example.utils.IdParserUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.service.TopicService;
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
        Long topicId = null;
        try {
            topicId = IdParserUtils.parseId(req.getPathInfo());
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        User currentUser = (User) req.getSession().getAttribute("user");
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        Topic topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        List<Comment> comments = commentDAO.getCommentsByTopicId(topicId);
        Context context = createContextVal(topic, currentUser, loggedIn, comments);
        templateEngine.process("topic", context, resp.getWriter());
        log.info("Successfully processed GET request for topicId {}", topicId);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = (User) req.getSession().getAttribute("user");
        try {
            switch (action) {
                case "create-topic":
                    handleCreateTopic(req, resp, currentUser);
                    resp.sendRedirect(req.getContextPath() + "/forum");
                    break;
                case "like-topic":
                    handleLikeAction(req, currentUser);
                    resp.sendRedirect(req.getRequestURI());
                    break;
                case "dislike-topic":
                    handleDislikeAction(req, currentUser);
                    resp.sendRedirect(req.getRequestURI());
                    break;
                case "delete-topic":
                    handleDeleteTopic(req, currentUser);
                    resp.sendRedirect("/forum");
                    break;
            }
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error processing request", e);
            resp.sendRedirect(req.getContextPath() + "/error");
        }
    }

    private Context createContextVal(Topic topic, User currentUser, Boolean loggedIn, List<Comment> comments) {
        Context context = new Context();

        context.setVariable("topic", topic);
        context.setVariable("loggedIn", loggedIn);
        context.setVariable("currentUser", currentUser);
        context.setVariable("topicCreator", topic.getCreatedBy());
        context.setVariable("comments", comments);

        return context;
    }

    private void handleCreateTopic(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        String title = req.getParameter("title");
        String categoryIdStr = req.getParameter("categoryIdStr");
        String description = req.getParameter("description");

        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            log.warn("Missing or invalid input: title, description, or category ID is empty.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        Long categoryId = IdParserUtils.parseCategoryId(categoryIdStr);

        categoryDAO.getCategoryById(categoryId);
        topicDAO.addTopic(title, description, categoryId, currentUser);
        log.info("Successfully created topic with title: '{}' in category.", title);
    }


    private void handleLikeAction(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        topicService.updateRating(topicId, currentUser, true);
        log.info("User {} liked topic {}", currentUser, topicId);
    }


    private void handleDislikeAction(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        log.info("User {} disliked topic {}", currentUser, topicId);
        topicService.updateRating(topicId, currentUser, false);
    }

    private void handleDeleteTopic(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        topicDAO.deleteTopicById(topicId);
        log.info("User {} deleted topic {}", currentUser, topicId);
    }

}

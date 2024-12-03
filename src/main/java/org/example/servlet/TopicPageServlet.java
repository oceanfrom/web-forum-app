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
import org.example.service.CommentService;
import org.example.service.UserService;
import org.example.utils.IdParserUtils;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.service.TopicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/topic/*")
public class TopicPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicDAO topicDAO;
    private TopicService topicService;
    private CommentDAO commentDAO;
    private CategoryDAO categoryDAO;
    private UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicDAO = new TopicDAO();
        commentDAO = new CommentDAO();
        topicService = new TopicService();
        categoryDAO = new CategoryDAO();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long topicId = IdParserUtils.parseId(req.getPathInfo(), req, resp);
        User currentUser = userService.getCurrentUser(req);
        Boolean loggedIn = userService.isLoggedIn(req);
        Topic topic = topicDAO.getTopicById(topicId);
        if (topic == null) {
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        List<Comment> comments = commentDAO.getCommentsByTopicId(topicId);

        Map<String, Object> contextVal = new HashMap<>();
        contextVal.put("topic", topic);
        contextVal.put("loggedIn", loggedIn != null ? loggedIn : false);
        contextVal.put("currentUser", currentUser != null ? currentUser : new User());
        contextVal.put("topicCreator", topic.getCreatedBy() != null ? topic.getCreatedBy() : new User());
        contextVal.put("comments", comments != null ? comments : new ArrayList<>());
        Context context = ThymeleafContextUtils.createContext(contextVal);
        templateEngine.process("topic", context, resp.getWriter());
        log.info("Successfully processed GET request for topicId {}", topicId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = userService.getCurrentUser(req);

        switch (action) {
            case "create":
                topicService.handleCreateTopic(req, resp, currentUser);
                resp.sendRedirect(req.getContextPath() + "/forum");
                break;
            case "like":
                topicService.handleLikeAction(req, resp, currentUser);
                resp.sendRedirect(req.getRequestURI());
                break;
            case "dislike":
                topicService.handleDislikeAction(req, resp, currentUser);
                resp.sendRedirect(req.getRequestURI());
                break;
            case "deleteTopic":
                topicService.handleDeleteTopic(req, resp, currentUser);
                resp.sendRedirect("/forum");
                break;
            default:
                resp.sendRedirect("/forum");
                break;
        }
    }
}

package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.CommentSerivce;
import org.example.service.TopicService;
import org.example.utils.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.List;

@WebServlet("/topic/*")
public class TopicPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private CommentSerivce commentSerivce;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicService();
        commentSerivce = new CommentSerivce();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Topic ID is missing");
            return;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Long topicId = Long.parseLong(path);
        User currentUser = (User) req.getSession().getAttribute("user");
        Topic topic = topicService.getTopicById(topicId);
        if (topic == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Topic not found");
            return;
        }
        List<Comment> comments = commentSerivce.getCommentsByTopicId(topicId);
        User topicCreator = topic.getCreatedBy();

        Context context = new Context();
        context.setVariable("topic", topic);
        context.setVariable("loggedIn", currentUser != null);
        context.setVariable("topicCreator", topicCreator);
        context.setVariable("comments", comments);
        resp.setContentType("text/html");
        templateEngine.process("topic", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String topicIdString = req.getParameter("topicId");
        if (topicIdString == null || topicIdString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Topic ID is missing");
            return;
        }
        Long topicId = Long.parseLong(topicIdString);
        User currentUser = (User) req.getSession().getAttribute("user");

        Topic topic = topicService.getTopicById(topicId);
        if (topic == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Topic not found");
            return;
        }

        if ("like".equals(action))
            topicService.updateRating(topicId, currentUser, true);
        else if ("dislike".equals(action))
            topicService.updateRating(topicId, currentUser, false);

        resp.sendRedirect(req.getRequestURI());
    }
}



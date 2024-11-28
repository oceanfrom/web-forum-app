package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.TopicService;
import org.example.utils.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet("/topic/*")
public class TopicPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty())
            return;

        if (path.startsWith("/")) {
            path = path.substring(1);

            Long topicId = Long.parseLong(path);
            User currentUser = (User) req.getSession().getAttribute("user");
            Topic topic = topicService.getTopicById(topicId);
            Context context = new Context();
            context.setVariable("topic", topic);
            context.setVariable("loggedIn", currentUser != null);
            resp.setContentType("text/html");
            templateEngine.process("topic", context, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String path = req.getPathInfo();
        if (path == null || path.isEmpty())
            return;

        if (path.startsWith("/"))
            path = path.substring(1);

        Long topicId = Long.parseLong(path);
        User currentUser = (User) req.getSession().getAttribute("user");

        if("like".equals(action))
            topicService.updateRating(topicId, currentUser, true);
        else if("dislike".equals(action))
            topicService.updateRating(topicId, currentUser, false);

        resp.sendRedirect(req.getRequestURI());

    }
}

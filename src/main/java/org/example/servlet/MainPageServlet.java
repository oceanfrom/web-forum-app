package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.service.TopicService;
import org.example.utils.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.List;

@WebServlet("/forum")
public class MainPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;


    @Override
    public void init() {
        ThymeleafConfig config = new ThymeleafConfig();
        templateEngine = config.createTemplateEngine();
        topicService = new TopicService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Topic> topics = topicService.getAllTopics();
        Context context = new Context();
        context.setVariable("topics", topics);
        resp.setContentType("text/html");
        templateEngine.process("index", context, resp.getWriter());
    }
}

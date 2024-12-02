package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;
import org.example.dao.CategoryDAO;
import org.example.dao.TopicDAO;
import org.example.config.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/forum")
public class MainPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicDAO topicDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicDAO = new TopicDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        List<Topic> topics = topicDAO.getAllTopics();
        List<Category> categories = categoryDAO.getAllCategories();

        Context context = new Context();
        context.setVariable("topics", topics);
        context.setVariable("user", currentUser);
        context.setVariable("loggedIn", loggedIn);
        context.setVariable("categories", categories);
        resp.setContentType("text/html");
        templateEngine.process("index", context, resp.getWriter());
        log.info("Successfully rendered /forum page.");
    }
}

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
import org.example.service.CategoryService;
import org.example.service.TopicService;
import org.example.utils.IdParserUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/forum")
public class MainPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private CategoryService categoryService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = (User) req.getSession().getAttribute("user");
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        List<Category> categories = categoryService.getAllCategories();
        List<Topic> topics = getTopicsBasedOnAction(action, req, resp);

        Context context = createContextVal(topics, categories, currentUser, loggedIn);
        templateEngine.process("index", context, resp.getWriter());
        log.info("Successfully rendered /forum page with {} topics.", topics.size());

    }

    private List<Topic> getTopicsBasedOnAction(String action, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (action == null) {
            log.warn("Action parameter is missing, defaulting to 'all-topics'.");
            action = "all-topics";
        }
        switch (action) {
            case "most-popular":
                log.info("Get most popular topics.");
                return topicService.getAllTopicsByLikes();

            case "least-popular":
                log.info("Get least popular topics.");
                return topicService.getAllTopicsByDislikes();

            case "by-category":
                Long categoryId = null;
                try {
                    categoryId = IdParserUtils.parseId(req.getParameter("category"));
                } catch (IdParserUtils.InvalidIdException e) {
                    log.error("Error parsing ID", e);
                    resp.sendRedirect(req.getContextPath() + "/error");
                    return null;
                }
                log.info("Get topics for category ID: {}", categoryId);
                return topicService.getAllTopicsByCategoryId(categoryId);

            case "all-topics":
                log.info("Get all topics.");
                return topicService.getAllTopics();
        }

        return topicService.getAllTopics();
    }


    private Context createContextVal(List<Topic> topics, List<Category> categories, User currentUser, Boolean loggedIn) {
        Context context = new Context();
        context.setVariable("topics", topics);
        context.setVariable("categories", categories);
        context.setVariable("user", currentUser);
        context.setVariable("loggedIn", loggedIn);
        return context;
    }
}

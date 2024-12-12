package org.example.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.CategoryService;
import org.example.service.TopicService;
import org.example.service.impl.CategoryServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
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
    private ContextGeneration contextGeneration;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicServiceImpl();
        categoryService = new CategoryServiceImpl();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = (User) req.getSession().getAttribute("user");
        Boolean loggedIn = (Boolean) req.getSession().getAttribute("loggedIn");
        String searchQuery = req.getParameter("search");
        List<Category> categories = categoryService.getAllCategories();
        List<Topic> topics = getTopicsBasedOnAction(action, req, resp);

        if(searchQuery != null && !searchQuery.isEmpty()) {
            topics = topicService.searchByTitle(searchQuery);
            log.info("Search: {}; Topic founds: {}", searchQuery, topics.size());
        }

        Context context = contextGeneration.createContext("topics", topics, "categories", categories,
                "user", currentUser, "loggedIn", loggedIn);        templateEngine.process("index", context, resp.getWriter());
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
                    resp.sendRedirect("/error");
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
}

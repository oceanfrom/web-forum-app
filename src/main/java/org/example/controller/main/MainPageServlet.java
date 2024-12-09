package org.example.controller.main;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.actions.SortingHandler;
import org.example.service.actions.impl.sort.AllTopicsHandlerImpl;
import org.example.service.actions.impl.sort.LeastPopularTopicsHandlerImpl;
import org.example.service.actions.impl.sort.MostPopularTopicsHandlerImpl;
import org.example.service.actions.impl.sort.TopicsByCategoryHandlerImpl;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.CategoryService;
import org.example.service.TopicService;
import org.example.service.impl.CategoryServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.controller.BaseServlet;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/forum")
public class MainPageServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private CategoryService categoryService;
    private ContextGeneration contextGeneration;
    private Map<String, SortingHandler> actions;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicServiceImpl();
        categoryService = new CategoryServiceImpl();
        contextGeneration = new ContextGenerationImpl();

        actions = new HashMap<>();
        actions.put("most-popular", new MostPopularTopicsHandlerImpl());
        actions.put("least-popular", new LeastPopularTopicsHandlerImpl());
        actions.put("by-category", new TopicsByCategoryHandlerImpl());
        actions.put("all-topics", new AllTopicsHandlerImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "all-topics";
        }
        User currentUser = getCurrentUser(req);
        Boolean loggedIn = isUserLoggedIn(req);
        String searchQuery = req.getParameter("search");
        List<Category> categories = categoryService.getAllCategories();

        SortingHandler handler = actions.get(action);
        List<Topic> topics;
        if (handler != null) {
            try {
                topics = handler.getTopics(req, resp);
            } catch (Exception e) {
                log.error("Error occurred while fetching topics", e);
                handleError(resp);
                return;
            }
        } else {
            log.warn("Unknown action: {}", action);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }

        if (searchQuery != null && !searchQuery.isEmpty()) {
            topics = topicService.searchByTitle(searchQuery);
            log.info("Search: {}; Topic founds: {}", searchQuery, topics.size());
        }

        Context context = contextGeneration.createContext("topics", topics, "categories", categories,
                "user", currentUser, "loggedIn", loggedIn);
        templateEngine.process("index", context, resp.getWriter());
        log.info("Successfully rendered /forum page with {} topics.", topics.size());

    }

}

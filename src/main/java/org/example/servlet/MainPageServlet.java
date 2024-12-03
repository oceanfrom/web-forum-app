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
import org.example.service.UserService;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/forum")
public class MainPageServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private TopicDAO topicDAO;
    private CategoryDAO categoryDAO;
    private UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicDAO = new TopicDAO();
        categoryDAO = new CategoryDAO();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = userService.getCurrentUser(req);
        Boolean loggedIn = userService.isLoggedIn(req);
        List<Topic> topics = topicDAO.getAllTopics();
        List<Category> categories = categoryDAO.getAllCategories();

        Map<String, Object> contextVal = new HashMap<>();
        contextVal.put("topics", topics != null ? topics : new ArrayList<>());
        contextVal.put("user", currentUser != null ? currentUser : new User());
        contextVal.put("loggedIn", loggedIn != null ? loggedIn : false);
        contextVal.put("categories", categories != null ? categories : new ArrayList<>());
        Context context = ThymeleafContextUtils.createContext(contextVal);
        templateEngine.process("index", context, resp.getWriter());
        log.info("Successfully rendered /forum page.");
    }
}

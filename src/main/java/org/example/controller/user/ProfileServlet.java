package org.example.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.actions.ActionHandler;
import org.example.service.actions.manager.ActionManagerImpl;
import org.example.service.actions.impl.user.DeleteUserHandler;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.TopicService;
import org.example.service.UserService;
import org.example.service.impl.TopicServiceImpl;
import org.example.service.impl.UserServiceImpl;
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
@WebServlet("/profile/*")
public class ProfileServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private UserService userService;
    private TopicService topicService;
    private ContextGeneration contextGeneration;
    private ActionManagerImpl actionManagerImpl;
    private Map<String, ActionHandler> actions;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserServiceImpl();
        topicService = new TopicServiceImpl();
        contextGeneration = new ContextGenerationImpl();
        actions = new HashMap<>();
        actions.put("delete-user", new DeleteUserHandler());
        actionManagerImpl = new ActionManagerImpl(actions);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = getCurrentUser(req);
        try {
            ensureUserLoggedIn(resp,currentUser);
        } catch (IOException e) {
            return;
        }

        Long userId = parseIdWithSubstring(req,resp);
        User user = userService.getUserById(userId);
        if (user == null) {
            log.warn("User not found with ID: {}", userId);
            handleError(resp);
            return;
        }
        if (currentUser.getId().equals(user.getId())) {
            log.info("Current user is the same as the profile user, redirecting to /account");
            handleRedirect(resp, "/account");
            return;
        }
        List<Topic> createdTopics = topicService.getCreatedTopicsByUser(user.getId());

        Context context = contextGeneration.createContext("currentUser", currentUser, "user", user,
                "createdTopics", createdTopics);
        templateEngine.process("profile", context, resp.getWriter());
        log.info("Successfully rendered profile page for user {}", userId);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User currentUser = getCurrentUser(req);
        actionManagerImpl.handleAction(req,resp, currentUser);
    }

}

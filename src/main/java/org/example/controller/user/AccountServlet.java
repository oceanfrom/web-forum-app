package org.example.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.actions.AccountActionHandler;
import org.example.service.actions.impl.account.UpdateAccountEmailHandlerImpl;
import org.example.service.actions.impl.account.UpdateAccountNameHandlerImpl;
import org.example.service.actions.impl.account.UpdateAccountPasswordHandlerImpl;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.TopicService;
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
@WebServlet("/account/*")
public class AccountServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private ContextGeneration contextGeneration;
    private Map<String, AccountActionHandler> actions;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        topicService = new TopicServiceImpl();
        contextGeneration = new ContextGenerationImpl();
        actions = new HashMap<>();
        actions.put( "update-username", new UpdateAccountNameHandlerImpl());
        actions.put( "update-password", new UpdateAccountPasswordHandlerImpl());
        actions.put( "update-email", new UpdateAccountEmailHandlerImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        try {
            ensureUserLoggedIn(resp, currentUser);
        } catch (IOException e) {
            return;
        }
        List<Topic> createdTopics = topicService.getCreatedTopicsByUser(currentUser.getId());
        List<Topic> likedTopics = topicService.getLikedTopicsByUser(currentUser.getId());

        Context context = contextGeneration.createContext("createdTopics", createdTopics, "likedTopics",
                likedTopics, "user", currentUser);
        templateEngine.process("account", context, resp.getWriter());
        log.info("Successfully rendered account page for user {}", currentUser.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String errorMessage = "";
        User currentUser = getCurrentUser(req);

        AccountActionHandler handler = actions.get(action);
        if (handler != null) {
            errorMessage = handler.execute(req, currentUser);
        } else {
            errorMessage = "Invalid action.";
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            log.warn("Error updating account settings: {}", errorMessage);
            Context context = contextGeneration.createContext("user", currentUser, "errorMessage", errorMessage);
            templateEngine.process("account", context, resp.getWriter());
        } else {
            log.info("Changes saved successfully for user {}", currentUser.getId());
            resp.sendRedirect("/account");
        }
    }
}

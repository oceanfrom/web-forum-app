package org.example.controller.topic;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.service.actions.ActionManager;
import org.example.service.actions.manager.ActionManagerImpl;
import org.example.service.actions.impl.topic.CreateTopicHandler;
import org.example.service.actions.impl.topic.DeleteTopicHandler;
import org.example.service.actions.impl.topic.DislikeTopicHandler;
import org.example.service.actions.impl.topic.LikeTopicHandler;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.config.ThymeleafConfig;
import org.example.service.CommentService;
import org.example.service.impl.CommentServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.controller.BaseServlet;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.service.TopicService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebServlet("/topic/*")
public class TopicPageServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private TopicService topicService;
    private CommentService commentService;
    private ContextGeneration contextGeneration;
    private ActionManager actionManager;
    private Map<String, ActionHandler> actions;


    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        commentService = new CommentServiceImpl();
        topicService = new TopicServiceImpl();
        contextGeneration = new ContextGenerationImpl();
        actions = new HashMap<>();
        actions.put("create-topic", new CreateTopicHandler());
        actions.put("like-topic", new LikeTopicHandler());
        actions.put("dislike-topic", new DislikeTopicHandler());
        actions.put("delete-topic", new DeleteTopicHandler());
        actionManager = new ActionManagerImpl(actions);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long topicId = parseIdWithSubstring(req,resp);
        if (topicId == null) return;

        User currentUser = getCurrentUser(req);
        Boolean loggedIn = isUserLoggedIn(req);

        Topic topic = topicService.getTopicById(topicId);
        if (topic == null) {
            handleError(resp);
            return;
        }
        List<Comment> comments = commentService.getCommentsByTopicId(topicId);
        Context context = contextGeneration.createContext("topic", topic, "loggedIn", loggedIn,
                "currentUser", currentUser, "topicCreator", topic.getCreatedBy(), "comments", comments);
        templateEngine.process("topic", context, resp.getWriter());
        log.info("Successfully processed GET request for topicId {}", topicId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = getCurrentUser(req);
        actionManager.handleAction(req, resp, currentUser);
    }
}

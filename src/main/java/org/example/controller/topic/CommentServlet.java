package org.example.controller.topic;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.service.actions.manager.ActionManagerImpl;
import org.example.service.actions.impl.comment.CreateCommentHandlerImpl;
import org.example.service.actions.impl.comment.DeleteCommentHandlerImpl;
import org.example.service.actions.impl.comment.DislikeCommentHandlerImpl;
import org.example.service.actions.impl.comment.LikeCommentHandlerImpl;
import org.example.model.User;
import org.example.controller.BaseServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet("/comment/*")
public class CommentServlet extends BaseServlet {
    private ActionManagerImpl actionManagerImpl;
    private Map<String, ActionHandler> actions;

    @Override
    public void init(ServletConfig config) {
        actions = new HashMap<>();
        actions.put("like-comment", new LikeCommentHandlerImpl());
        actions.put("dislike-comment", new DislikeCommentHandlerImpl());
        actions.put("delete-comment", new DeleteCommentHandlerImpl());
        actions.put("add-comment", new CreateCommentHandlerImpl());
        actionManagerImpl = new ActionManagerImpl(actions);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = getCurrentUser(req);
        actionManagerImpl.handleAction(req, resp, currentUser);
    }
}


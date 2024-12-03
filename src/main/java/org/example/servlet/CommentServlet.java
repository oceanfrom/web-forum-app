package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.CommentService;
import org.example.service.UserService;
import org.example.utils.IdParserUtils;
import java.io.IOException;

@Slf4j
@WebServlet("/comment/*")
public class CommentServlet extends HttpServlet {
    private CommentService commentService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        commentService = new CommentService();
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"), req, resp);
        User currentUser = userService.getCurrentUser(req);

        switch (action) {
            case "likeComm":
               commentService.handleLikeComment(req, resp, currentUser, topicId);
                resp.sendRedirect("/topic/" + topicId);
                break;
            case "dislikeComm":
                commentService.handleDislikeComment(req, resp, currentUser, topicId);
                resp.sendRedirect("/topic/" + topicId);
                break;
            case "deleteComm":
                commentService.handleDeleteComment(req, resp, currentUser, topicId);
                resp.sendRedirect("/topic/" + topicId);
                break;
            case "addComment":
                commentService.handleAddComment(req, resp, currentUser, topicId);
                resp.sendRedirect("/topic/" + topicId);
                break;
            default:
                log.warn("Unknown action: {}", action);
                break;
        }
    }
}


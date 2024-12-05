package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.CommentService;
import org.example.service.TopicService;
import org.example.utils.IdParserUtils;
import java.io.IOException;

@Slf4j
@WebServlet("/comment/*")
public class CommentServlet extends HttpServlet {
    private CommentService commentService;
    private TopicService topicService;

    @Override
    public void init(ServletConfig config) {
        commentService = new CommentService();
        topicService = new TopicService();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        try {
            Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
            User currentUser = (User) req.getSession().getAttribute("user");

            switch (action) {
                case "like-comment":
                    handleLikeComment(req, currentUser);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
                case "dislike-comment":
                    handleDislikeComment(req, currentUser);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
                case "delete-comment":
                    handleDeleteComment(req, currentUser);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
                case "add-comment":
                    handleAddComment(req, currentUser, topicId);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
            }
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            resp.sendRedirect(req.getContextPath() + "/error");
        }
    }


    private void handleLikeComment(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' liked comment with ID: {}", currentUser.getUsername(), commentId);
        commentService.updateRating(commentId, currentUser, true);
    }

    private void handleDislikeComment(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' disliked comment with ID: {}", currentUser.getUsername(), commentId);
        commentService.updateRating(commentId, currentUser, false);
    }

    private void handleDeleteComment(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' is deleting comment with ID: {}", currentUser.getUsername(), commentId);
        commentService.deteleCommentById(commentId);
    }

    private void handleAddComment(HttpServletRequest req, User currentUser, Long topicId) {
        String content = req.getParameter("commentContent");
        Topic topic = topicService.getTopicById(topicId);
        commentService.createComment(content, currentUser, topic);
        log.info("User '{}' added a comment to topic ID: {}", currentUser.getUsername(), topicId);
    }

}


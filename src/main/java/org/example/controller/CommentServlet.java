package org.example.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.CommentRatingService;
import org.example.service.CommentService;
import org.example.service.TopicService;
import org.example.service.impl.CommentRatingServiceImpl;
import org.example.service.impl.CommentServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;

import java.io.IOException;

@Slf4j
@WebServlet("/comment/*")
public class CommentServlet extends HttpServlet {
    private CommentService commentService;
    private TopicService topicService;
    private CommentRatingService commentRatingService;

    @Override
    public void init(ServletConfig config) {
        commentService = new CommentServiceImpl();
        topicService = new TopicServiceImpl();
        commentRatingService = new CommentRatingServiceImpl();
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
                    handleDeleteComment(req, resp, currentUser);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
                case "add-comment":
                    handleAddComment(req, currentUser, topicId);
                    resp.sendRedirect("/topic/" + topicId);
                    break;
            }
        } catch (Exception e) {
            log.error("Error during actions", e);
            resp.sendRedirect("/error");
        }
    }


    private void handleLikeComment(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' liked comment with ID: {}", currentUser.getUsername(), commentId);
        commentRatingService.updateRating(commentId, currentUser, true);
    }

    private void handleDislikeComment(HttpServletRequest req, User currentUser) throws IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' disliked comment with ID: {}", currentUser.getUsername(), commentId);
        commentRatingService.updateRating(commentId, currentUser, false);
    }

    private void handleDeleteComment(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IdParserUtils.InvalidIdException, IOException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        Comment comment = commentService.getCommentById(commentId);
        if (comment.getCreatedBy().getId().equals(currentUser.getId()) || "admin".equalsIgnoreCase(currentUser.getRole().toString())) {
            commentService.deleteCommentById(commentId);
            log.info("User '{}' deleted comment '{}'", currentUser.getUsername(), commentId);
        } else {
            log.warn("User '{}' attempted to delete comment '{}' without correct rights", currentUser.getUsername(), commentId);
            resp.sendRedirect("/error");
        }
    }

    private void handleAddComment(HttpServletRequest req, User currentUser, Long topicId) {
        String content = req.getParameter("commentContent");
        Topic topic = topicService.getTopicById(topicId);
        commentService.createComment(content, currentUser, topic);
        log.info("User '{}' added a comment to topic ID: {}", currentUser.getUsername(), topicId);
    }

}


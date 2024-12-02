package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.dao.CommentDAO;
import org.example.service.CommentService;

import java.io.IOException;

@Slf4j
@WebServlet("/comment/*")
public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO;
    private CommentService commentService;

    @Override
    public void init(ServletConfig config) {
        commentDAO = new CommentDAO();
        commentService = new CommentService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String content = req.getParameter("commentContent");
        String topicIdString = req.getParameter("topicId");

        if (topicIdString == null || topicIdString.isEmpty()) {
            log.warn("topicId is null or empty, redirecting to error page.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        Long topicId;
        try {
            topicId = Long.parseLong(topicIdString);
        } catch (NumberFormatException e) {
            log.error("Invalid topic ID format: {}", topicIdString, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        User currentUser = (User) req.getSession().getAttribute("user");

        String commentIdString = req.getParameter("commentId");
        if (commentIdString != null && !commentIdString.isEmpty()) {
            Long commentId = Long.parseLong(commentIdString);

            if ("likeComm".equals(action)) {
                log.info("User '{}' liked comment with ID: {}", currentUser.getUsername(), commentId);
                commentService.updateRating(commentId, currentUser, true);
            } else if ("dislikeComm".equals(action)) {
                log.info("User '{}' disliked comment with ID: {}", currentUser.getUsername(), commentId);
                commentService.updateRating(commentId, currentUser, false);
            } else if ("deleteComm".equals(action)) {
                log.info("User '{}' is deleting comment with ID: {}", currentUser.getUsername(), commentId);
                commentDAO.deteleComment(commentId);
            }
        } else if ("addComment".equals(action)) {
            log.info("User '{}' added a comment to topic ID: {}", currentUser.getUsername(), topicId);
            commentDAO.addComment(topicId, content, currentUser);
        }
        resp.sendRedirect("/topic/" + topicId);
    }
}


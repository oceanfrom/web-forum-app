package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.CommentSerivce;
import org.example.service.TopicService;

import java.io.IOException;

@WebServlet("/comment/*")
public class CommentServlet extends HttpServlet {
    private CommentSerivce commentSerivce;
    private TopicService topicService;

    @Override
    public void init(ServletConfig config) {
        commentSerivce = new CommentSerivce();
        topicService = new TopicService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String content = req.getParameter("commentContent");
        String topicIdString = req.getParameter("topicId");

        if (topicIdString == null || topicIdString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Topic ID is missing");
            return;
        }
        Long topicId = Long.parseLong(topicIdString);
        User currentUser = (User) req.getSession().getAttribute("user");

        Topic topic = topicService.getTopicById(topicId);
        if (topic == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Topic not found");
            return;
        }

        String commentIdString = req.getParameter("commentId");
        if (commentIdString != null && !commentIdString.isEmpty()) {
            Long commentId = Long.parseLong(commentIdString);

            if ("likeComm".equals(action))
                commentSerivce.updateRating(commentId, currentUser, true);
            else if ("dislikeComm".equals(action))
                commentSerivce.updateRating(commentId, currentUser, false);
        } else if ("addComment".equals(action))
            commentSerivce.addComment(topicId, content, currentUser);

        resp.sendRedirect("/topic/" + topicId);
    }
}


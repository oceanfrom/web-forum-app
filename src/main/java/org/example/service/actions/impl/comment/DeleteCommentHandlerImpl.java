package org.example.service.actions.impl.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.CommentService;
import org.example.service.TopicService;
import org.example.service.impl.CommentServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;

import java.io.IOException;

@Slf4j
public class DeleteCommentHandlerImpl implements ActionHandler {
    CommentService commentService = new CommentServiceImpl();
    TopicService topicService = new TopicServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        Topic topic = topicService.getTopicById(topicId);
        if (topic.getCreatedBy().equals(currentUser) || "admin".equalsIgnoreCase(currentUser.getRole().toString())) {
            commentService.deleteCommentById(commentId);
            log.info("User '{}' deleted comment '{}'", currentUser.getUsername(), commentId);
        } else {
            log.warn("User '{}' attempted to delete comment '{}' without correct rights", currentUser.getUsername(), commentId);
            throw new SecurityException("Unauthorized action");
        }
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        return "/topic/" + topicId;
    }
}

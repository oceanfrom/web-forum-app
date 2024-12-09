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
public class CreateCommentHandlerImpl implements ActionHandler {
    private final TopicService topicService = new TopicServiceImpl();
    private final CommentService commentService = new CommentServiceImpl();


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        String content = req.getParameter("commentContent");
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        Topic topic = topicService.getTopicById(topicId);
        commentService.createComment(content, currentUser, topic);
        log.info("User '{}' added a comment to topic ID: {}", currentUser.getUsername(), topicId);
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        return "/topic/" + topicId;
    }
}

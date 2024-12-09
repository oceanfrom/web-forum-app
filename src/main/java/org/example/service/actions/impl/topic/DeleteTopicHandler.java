package org.example.service.actions.impl.topic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.Topic;
import org.example.model.User;
import org.example.service.TopicService;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;
import java.io.IOException;

@Slf4j
public class DeleteTopicHandler implements ActionHandler {
    private final TopicService topicService = new TopicServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        Topic topic = topicService.getTopicById(topicId);
        if (topic.getCreatedBy().equals(currentUser) || "admin".equalsIgnoreCase(currentUser.getRole().toString())) {
            topicService.deleteTopicById(topicId);
            log.info("User {} deleted topic {}", currentUser.getUsername(), topicId);
        } else {
            log.warn("User {} attempted to delete topic {} without correct rights", currentUser.getUsername(), topicId);
            throw new SecurityException("Unauthorized action");
        }
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req){
        return "/forum";
    }
}

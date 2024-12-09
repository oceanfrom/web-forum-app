package org.example.service.actions.impl.topic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.User;
import org.example.service.TopicRatingService;
import org.example.service.impl.TopicRatingServiceImpl;
import org.example.utils.IdParserUtils;

@Slf4j
public class DislikeTopicHandler implements ActionHandler {
    private final TopicRatingService topicRatingService = new TopicRatingServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        log.info("User {} disliked topic {}", currentUser, topicId);
        topicRatingService.updateRating(topicId, currentUser, false);
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        return "/topic/" + topicId;
    }
}

package org.example.service.actions.impl.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.User;
import org.example.service.CommentRatingService;
import org.example.service.impl.CommentRatingServiceImpl;
import org.example.utils.IdParserUtils;

import java.io.IOException;

@Slf4j
public class LikeCommentHandlerImpl implements ActionHandler {
    private final CommentRatingService commentRatingService = new CommentRatingServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        Long commentId = IdParserUtils.parseId(req.getParameter("commentId"));
        log.info("User '{}' liked comment with ID: {}", currentUser.getUsername(), commentId);
        commentRatingService.updateRating(commentId, currentUser, true);
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"));
        return "/topic/" + topicId;
    }
}

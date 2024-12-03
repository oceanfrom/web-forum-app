package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.CategoryDAO;
import org.example.dao.TopicDAO;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.example.transaction.SessionManager;
import org.example.utils.IdParserUtils;
import org.hibernate.Session;

import java.io.IOException;

@Slf4j
public class TopicService {
    private TopicDAO topicDAO = new TopicDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public void handleCreateTopic(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        String title = req.getParameter("title");
        String categoryIdStr = req.getParameter("categoryIdStr");
        String description = req.getParameter("description");

        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            log.warn("Missing or invalid input: title, description, or category ID is empty.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        Long categoryId = IdParserUtils.parseCategoryId(categoryIdStr, resp, req);

        categoryDAO.getCategoryById(categoryId);
        topicDAO.addTopic(title, description, categoryId, currentUser);
        log.info("Successfully created topic with title: '{}' in category.", title);
    }


    public void handleLikeAction(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"), req, resp);
        log.info("User {} liked topic {}", currentUser, topicId);
        updateRating(topicId, currentUser, true);
    }

    public void handleDislikeAction(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"), req, resp);
        log.info("User {} disliked topic {}", currentUser, topicId);
        updateRating(topicId, currentUser, false);
    }

    public void handleDeleteTopic(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long topicId = IdParserUtils.parseId(req.getParameter("topicId"), req, resp);
        topicDAO.deleteTopicById(topicId);
        log.info("User {} deleted topic {}", currentUser, topicId);
    }

    public void updateRating(Long topicId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Topic topic = session.get(Topic.class, topicId);
            TopicRating existingRating = topicDAO.findExistingRating(session, topicId, user.getId());
            if (existingRating != null)
                processExistingRating(session, topic, existingRating, isLike);
            else
                addNewRating(session, topic, user, isLike);
        });
    }

    private void changeTopicRating(Topic topic, int like, int dislike) {
        topic.setLikes(topic.getLikes() + like);
        topic.setDislikes(topic.getDislikes() + dislike);
    }

    private void processExistingRating(Session session, Topic topic, TopicRating existingRating, boolean isLike) {
        if (existingRating.isLike() == isLike) {
            session.delete(existingRating);
            if (isLike)
                changeTopicRating(topic, -1, 0);
            else
                changeTopicRating(topic, 0, -1);
        } else {
            existingRating.setLike(isLike);
            session.update(existingRating);
            if (isLike)
                changeTopicRating(topic, 1, -1);
            else
                changeTopicRating(topic, -1, 1);
        }
    }

    private void addNewRating(Session session, Topic topic, User user, boolean isLike) {
        TopicRating newRating = new TopicRating();
        newRating.setUser(user);
        newRating.setTopic(topic);
        newRating.setLike(isLike);
        session.save(newRating);
        if (isLike)
            changeTopicRating(topic, 1, 0);
        else
            changeTopicRating(topic, 0, 1);
    }
}

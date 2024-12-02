package org.example.service;

import org.example.dao.TopicDAO;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

public class TopicService {
    private TopicDAO topicDAO = new TopicDAO();

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

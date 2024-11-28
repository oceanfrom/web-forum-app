package org.example.service;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.example.utils.impl.SessionManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TopicService {
    public List<Topic> getAllTopics() {
        return SessionManager.executeInTransaction(session -> {
            Query<Topic> query = session.createQuery(
                    "SELECT t FROM Topic t " +
                            "LEFT JOIN FETCH t.createdBy ", Topic.class);
            List<Topic> topics = query.getResultList();
            return topics;
        });
    }

    public Topic getTopicById(Long id) {
        return SessionManager.executeInTransaction(session -> session.get(Topic.class, id));

    }

    public void updateRating(Long topicId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Topic topic = session.get(Topic.class, topicId);
            TopicRating existingRating = findExistingRating(session, topicId, user.getId());
            if (existingRating != null)
                processExistingRating(session, topic, existingRating, isLike);
            else
                addNewRating(session, topic, user, isLike);
        });
    }

    private TopicRating findExistingRating(Session session, Long topicId, Long userId) {
        Query<TopicRating> query = session.createQuery(
                "FROM TopicRating t WHERE t.user.id = :userId AND t.topic.id = :topicId", TopicRating.class);
        query.setParameter("userId", userId);
        query.setParameter("topicId", topicId);
        return query.uniqueResultOptional().orElse(null);
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

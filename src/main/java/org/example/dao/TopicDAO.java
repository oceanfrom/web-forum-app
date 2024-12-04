package org.example.dao;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.transaction.SessionManager;
import org.hibernate.Session;
import java.util.List;

public class TopicDAO {

    public void addTopicById(Topic topic) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            session.save(topic);
        });
    }

    public void deleteTopicById(Long topicId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Topic topic = session.get(Topic.class, topicId);
            if (topic == null)
                return;
            session.delete(topic);
        });
    }

    public List<Topic> getCreatedTopicsByUser(Long userId) {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery(
                    "SELECT t FROM Topic t WHERE t.createdBy.id = :userId", Topic.class)
                    .setParameter("userId", userId).getResultList();
        });
    }

    public List<Topic> getLikedTopicsByUser(Long userId) {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery(
                    "SELECT t.topic FROM TopicRating t WHERE t.user.id = :userId AND t.like = true", Topic.class)
            .setParameter("userId", userId).getResultList();
        });
    }

    public List<Topic> getAllTopics() {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery(
                    "SELECT t FROM Topic t", Topic.class).getResultList();
        });
    }

    public Topic getTopicById(Long id) {
        return SessionManager.executeInTransaction(session -> session.get(Topic.class, id));
    }

    public TopicRating findExistingRating(Session session, Long topicId, Long userId) {
       return session.createQuery(
                "SELECT t FROM TopicRating t WHERE t.user.id = :userId AND t.topic.id = :topicId", TopicRating.class)
        .setParameter("userId", userId)
        .setParameter("topicId", topicId).uniqueResult();
    }
}

package org.example.dao;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.transaction.SessionManager;
import org.hibernate.Session;
import java.util.List;

public class TopicDAO {

    public List<Topic> searchByTitle(String title) {
       return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(:title)")
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
        });
    }

    public void saveTopic(Topic topic) {
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

    public List<Topic> getAllTopicsByLikes() {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT t FROM Topic t ORDER BY t.likes DESC", Topic.class)
                    .getResultList();
        });
    }

    public List<Topic> getAllTopicsByDislikes() {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("SELECT t FROM Topic t ORDER BY t.dislikes DESC", Topic.class)
                    .getResultList();
        });
    }

    public List<Topic> getAllTopicsByCategoryId(Long categoryId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                            "SELECT t FROM Topic t WHERE t.category.id = :categoryId", Topic.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
        });
    }

    public List<Topic> getCreatedTopicsByUser(Long userId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                    "SELECT t FROM Topic t WHERE t.createdBy.id = :userId", Topic.class)
                    .setParameter("userId", userId).getResultList();
        });
    }

    public List<Topic> getLikedTopicsByUser(Long userId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                    "SELECT t.topic FROM TopicRating t WHERE t.user.id = :userId AND t.like = true", Topic.class)
            .setParameter("userId", userId).getResultList();
        });
    }

    public List<Topic> getAllTopics() {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                    "SELECT t FROM Topic t", Topic.class).getResultList();
        });
    }

    public Topic getTopicById(Long id) {
        return SessionManager.executeReadOnly(session -> session.get(Topic.class, id));
    }

    public TopicRating findExistingRating(Session session, Long topicId, Long userId) {
       return session.createQuery(
                "SELECT t FROM TopicRating t WHERE t.user.id = :userId AND t.topic.id = :topicId", TopicRating.class)
        .setParameter("userId", userId)
        .setParameter("topicId", topicId).uniqueResult();
    }
}

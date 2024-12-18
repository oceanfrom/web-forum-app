package org.example.repository.impl;

import org.example.model.Topic;
import org.example.repository.TopicRepository;
import org.hibernate.Session;

import java.util.List;

public class TopicRepositoryImpl implements TopicRepository {

    @Override
    public List<Topic> searchByTitle(Session session, String title) {
        return session.createQuery("SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(:title)")
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    @Override
    public void saveTopic(Session session, Topic topic) {
        session.save(topic);
    }

    @Override
    public void deleteTopicById(Session session, Long topicId) {
        Topic topic = session.get(Topic.class, topicId);
        if (topic == null)
            return;
        session.delete(topic);
    }

    @Override
    public List<Topic> getAllTopicsByLikes(Session session) {
        return session.createQuery("SELECT t FROM Topic t ORDER BY t.likes DESC", Topic.class)
                .getResultList();
    }

    @Override
    public List<Topic> getAllTopicsByDislikes(Session session) {
        return session.createQuery("SELECT t FROM Topic t ORDER BY t.dislikes DESC", Topic.class)
                .getResultList();
    }

    @Override
    public List<Topic> getAllTopicsByCategoryId(Session session, Long categoryId) {
        return session.createQuery(
                        "SELECT t FROM Topic t WHERE t.category.id = :categoryId", Topic.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    @Override
    public List<Topic> getCreatedTopicsByUser(Session session, Long userId) {
        return session.createQuery(
                        "SELECT t FROM Topic t WHERE t.createdBy.id = :userId", Topic.class)
                .setParameter("userId", userId).
                getResultList();
    }

    @Override
    public List<Topic> getLikedTopicsByUser(Session session, Long userId) {
        return session.createQuery(
                        "SELECT t.topic FROM TopicRating t WHERE t.user.id = :userId AND t.like = true", Topic.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Topic> getAllTopics(Session session) {
        return session.createQuery(
                        "SELECT t FROM Topic t", Topic.class)
                .getResultList();
    }

    @Override
    public Topic getTopicById(Session session, Long id) {
        return session.get(Topic.class, id);
    }

}

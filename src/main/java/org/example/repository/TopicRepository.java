package org.example.repository;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

import java.util.List;

public interface TopicRepository {
    List<Topic> searchByTitle(Session session, String title);

    void saveTopic(Session session, Topic topic);

    void deleteTopicById(Session session, Long topicId);

    List<Topic> getAllTopicsByLikes(Session session);

    List<Topic> getAllTopicsByDislikes(Session session);

    List<Topic> getAllTopicsByCategoryId(Session session, Long categoryId);

    List<Topic> getCreatedTopicsByUser(Session session, Long userId);

    List<Topic> getLikedTopicsByUser(Session session, Long userId);

    List<Topic> getAllTopics(Session session);

    Topic getTopicById(Session session, Long id);
}

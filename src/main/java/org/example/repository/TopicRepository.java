package org.example.repository;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

import java.util.List;

public interface TopicRepository {
    List<Topic> searchByTitle(String title);

    void saveTopic(Topic topic);

    void deleteTopicById(Long topicId);

    List<Topic> getAllTopicsByLikes();

    List<Topic> getAllTopicsByDislikes();

    List<Topic> getAllTopicsByCategoryId(Long categoryId);

    List<Topic> getCreatedTopicsByUser(Long userId);

    List<Topic> getLikedTopicsByUser(Long userId);

    List<Topic> getAllTopics();

    Topic getTopicById(Long id);
}

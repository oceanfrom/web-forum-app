package org.example.service;

import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;

import java.util.List;

public interface TopicService {
    List<Topic> searchByTitle(String title);

    List<Topic> getLikedTopicsByUser(Long id);

    List<Topic> getCreatedTopicsByUser(Long id);

    void deleteTopicById(Long id);

    Topic getTopicById(Long id);

    List<Topic> getAllTopicsByLikes();

    List<Topic> getAllTopicsByDislikes();

    List<Topic> getAllTopicsByCategoryId(Long id);

    List<Topic> getAllTopics();

    void createTopic(String title, String description, Category category, User user);
}

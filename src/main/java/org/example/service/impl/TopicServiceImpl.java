package org.example.service.impl;

import org.example.factory.TopicFactory;
import org.example.factory.impl.TopicFactoryImpl;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;
import org.example.repository.TopicRepository;
import org.example.repository.impl.TopicRepositoryImpl;
import org.example.service.TopicService;

import java.util.List;

public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository = new TopicRepositoryImpl();
    private final TopicFactory topicFactory = new TopicFactoryImpl();

    @Override
    public List<Topic> searchByTitle(String title) {
        return topicRepository.searchByTitle(title);
    }

    @Override
    public List<Topic> getLikedTopicsByUser(Long id) {
        return topicRepository.getLikedTopicsByUser(id);
    }

    @Override
    public List<Topic> getCreatedTopicsByUser(Long id) {
        return topicRepository.getCreatedTopicsByUser(id);
    }

    @Override
    public void deleteTopicById(Long id) {
        topicRepository.deleteTopicById(id);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.getTopicById(id);
    }

    @Override
    public List<Topic> getAllTopicsByLikes() {
        return topicRepository.getAllTopicsByLikes();
    }

    @Override
    public List<Topic> getAllTopicsByDislikes() {
        return topicRepository.getAllTopicsByDislikes();
    }

    @Override
    public List<Topic> getAllTopicsByCategoryId(Long id) {
        return topicRepository.getAllTopicsByCategoryId(id);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.getAllTopics();
    }

    @Override
    public void createTopic(String title, String description, Category category, User user) {
        Topic topic = topicFactory.createTopic(title, description, category, user);
        topicRepository.saveTopic(topic);
    }
}

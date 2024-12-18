package org.example.service.impl;

import org.example.factory.TopicFactory;
import org.example.factory.impl.TopicFactoryImpl;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;
import org.example.repository.TopicRepository;
import org.example.repository.impl.TopicRepositoryImpl;
import org.example.service.TopicService;
import org.example.transaction.SessionManager;

import java.util.List;

public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository = new TopicRepositoryImpl();
    private final TopicFactory topicFactory = new TopicFactoryImpl();

    @Override
    public List<Topic> searchByTitle(String title) {
       return SessionManager.executeReadOnly(session -> topicRepository.searchByTitle(session, title));
    }

    @Override
    public List<Topic> getLikedTopicsByUser(Long id) {
        return SessionManager.executeReadOnly(session -> topicRepository.getLikedTopicsByUser(session, id));
    }

    @Override
    public List<Topic> getCreatedTopicsByUser(Long id) {
        return SessionManager.executeReadOnly(session -> topicRepository.getCreatedTopicsByUser(session, id));
    }

    @Override
    public void deleteTopicById(Long id) {
        SessionManager.executeInTransactionWithoutReturn(session -> topicRepository.deleteTopicById(session, id));
    }

    @Override
    public Topic getTopicById(Long id) {
        return SessionManager.executeReadOnly(session -> topicRepository.getTopicById(session, id));
    }

    @Override
    public List<Topic> getAllTopicsByLikes() {
        return SessionManager.executeReadOnly(session -> topicRepository.getAllTopicsByLikes(session));
    }

    @Override
    public List<Topic> getAllTopicsByDislikes() {
        return SessionManager.executeReadOnly(session -> topicRepository.getAllTopicsByDislikes(session));
    }

    @Override
    public List<Topic> getAllTopicsByCategoryId(Long id) {
        return SessionManager.executeReadOnly(session -> topicRepository.getAllTopicsByCategoryId(session, id));
    }

    @Override
    public List<Topic> getAllTopics() {
       return SessionManager.executeReadOnly(session -> topicRepository.getAllTopics(session));
    }

    @Override
    public void createTopic(String title, String description, Category category, User user) {
       SessionManager.executeInTransactionWithoutReturn(session -> {
           Topic topic = topicFactory.createTopic(title, description, category, user);
           topicRepository.saveTopic(session, topic);
       });
    }
}

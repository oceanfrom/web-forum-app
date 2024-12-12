package org.example.factory.impl;

import org.example.factory.TopicFactory;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;

public class TopicFactoryImpl implements TopicFactory {
    @Override
    public Topic createTopic(String title, String description, Category category, User user) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(description);
        topic.setCategory(category);
        topic.setCreatedBy(user);
        return topic;
    }
}

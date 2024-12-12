package org.example.factory;

import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.User;

public interface TopicFactory {
    Topic createTopic(String title, String description, Category category, User user);
}

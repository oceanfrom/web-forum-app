package org.example.factory.impl;

import org.example.factory.TopicRatingFactory;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;

public class TopicRatingFactoryImpl implements TopicRatingFactory {

    @Override
    public TopicRating createTopicRating(Topic topic, User user, boolean isLike) {
        TopicRating newRating = new TopicRating();
        newRating.setUser(user);
        newRating.setTopic(topic);
        newRating.setLike(isLike);
        return newRating;
    }
}

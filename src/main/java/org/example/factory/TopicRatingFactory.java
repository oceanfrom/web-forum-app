package org.example.factory;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;

public interface TopicRatingFactory {
    TopicRating createTopicRating(Topic topic, User user, boolean isLike);
}

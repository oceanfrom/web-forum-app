package org.example.service;

import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.hibernate.Session;

public interface TopicRatingService {
    void updateRating(Long topicId, User user, boolean isLike);
}

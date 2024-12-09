package org.example.repository;

import org.example.model.TopicRating;
import org.hibernate.Session;

public interface TopicRatingRepository {
    TopicRating findExistingRating(Session session, Long topicId, Long userId);
}

package org.example.repository.impl;

import org.example.model.TopicRating;
import org.example.repository.TopicRatingRepository;
import org.hibernate.Session;

public class TopicRatingRepositoryImpl implements TopicRatingRepository {

    @Override
    public TopicRating findExistingRating(Session session, Long topicId, Long userId) {
        return session.createQuery(
                        "SELECT t FROM TopicRating t WHERE t.user.id = :userId AND t.topic.id = :topicId", TopicRating.class)
                .setParameter("userId", userId)
                .setParameter("topicId", topicId)
                .uniqueResult();
    }
}

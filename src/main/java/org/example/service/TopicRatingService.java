package org.example.service;
import org.example.model.User;

public interface TopicRatingService {
    void updateRating(Long topicId, User user, boolean isLike);
}

package org.example.service;

import org.example.model.User;

public interface CommentRatingService {
    void updateRating(Long commentId, User user, boolean isLike);
}

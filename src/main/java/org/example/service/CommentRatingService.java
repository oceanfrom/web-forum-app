package org.example.service;

import org.example.model.Comment;
import org.example.model.CommentRating;
import org.example.model.User;
import org.hibernate.Session;

public interface CommentRatingService {
    void updateRating(Long commentId, User user, boolean isLike);
}

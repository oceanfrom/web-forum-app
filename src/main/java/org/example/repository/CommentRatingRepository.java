package org.example.repository;

import org.example.model.CommentRating;
import org.hibernate.Session;

public interface CommentRatingRepository {
    CommentRating existingRating(Session session, Long commentId, Long userId);
}

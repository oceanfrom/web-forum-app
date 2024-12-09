package org.example.repository.impl;

import org.example.model.CommentRating;
import org.example.repository.CommentRatingRepository;
import org.hibernate.Session;

public class CommentRatingRepositoryImpl implements CommentRatingRepository {
    @Override
    public CommentRating existingRating(Session session, Long commentId, Long userId) {
        return session.createQuery(
                        "SELECT c FROM CommentRating c WHERE c.user.id = :userId AND c.comment.id = :commentId ", CommentRating.class)
                .setParameter("userId", userId)
                .setParameter("commentId", commentId).
                uniqueResult();
    }
}

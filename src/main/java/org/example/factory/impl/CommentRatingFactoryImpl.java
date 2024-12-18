package org.example.factory.impl;

import org.example.factory.CommentRatingFactory;
import org.example.model.Comment;
import org.example.model.CommentRating;
import org.example.model.User;

public class CommentRatingFactoryImpl implements CommentRatingFactory {
    @Override
    public CommentRating createCommentRating(Comment comment, User user, boolean isLike) {
        CommentRating newRating = new CommentRating();
        newRating.setUser(user);
        newRating.setComment(comment);
        newRating.setLike(isLike);
        return newRating;
    }
}

package org.example.factory;

import org.example.model.*;

public interface CommentRatingFactory {
    CommentRating createCommentRating(Comment comment, User user, boolean isLike);
}

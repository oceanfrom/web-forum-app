package org.example.service;

import org.example.model.*;
import org.example.utils.impl.SessionManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CommentSerivce {

    public Comment addComment(Long topicId, String content, User user) {
        return SessionManager.executeInTransaction(session -> {
            Topic topic = session.get(Topic.class, topicId);
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setTopic(topic);
            comment.setCreatedBy(user);
            comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            session.save(comment);
            return comment;
        });
    }

    public List<Comment> getCommentsByTopicId(Long topicId) {
        return SessionManager.executeInTransaction(session -> {
            Query<Comment> query = session.createQuery("from Comment where topic.id = :topicId", Comment.class);
            query.setParameter("topicId", topicId);
            return query.getResultList();
        });
    }

    public void updateRating(Long commentId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Comment comment = session.get(Comment.class, commentId);
            CommentRating existingRating = existingRating(session, commentId, user.getId());
            if (existingRating != null)
                processExistingRating(session, comment, existingRating, isLike);
            else
                addNewRating(session, comment, user, isLike);
        });
    }

    public CommentRating existingRating(Session session, Long commentId, Long userId) {
        Query<CommentRating> query = session.createQuery(
                "FROM CommentRating c WHERE c.user.id = :userId AND c.comment.id = :commentId ", CommentRating.class);
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        return query.uniqueResultOptional().orElse(null);
    }

    private void changeCommentRating(Comment comment, int like, int dislike) {
        comment.setLikes(comment.getLikes() + like);
        comment.setDislikes(comment.getDislikes() + dislike);
    }

    private void processExistingRating(Session session, Comment comment, CommentRating existingRating, boolean isLike) {
        if (existingRating.isLike() == isLike) {
            session.delete(existingRating);
            if (isLike)
                changeCommentRating(comment, -1, 0);
            else
                changeCommentRating(comment, 0, -1);
        } else {
            existingRating.setLike(isLike);
            session.update(existingRating);
            if (isLike)
                changeCommentRating(comment, 1, -1);
            else
                changeCommentRating(comment, -1, 1);
        }
    }

    private void addNewRating(Session session, Comment comment, User user, boolean isLike) {
        CommentRating newRating = new CommentRating();
        newRating.setUser(user);
        newRating.setComment(comment);
        newRating.setLike(isLike);
        session.save(newRating);
        if (isLike)
            changeCommentRating(comment, 1, 0);
        else
            changeCommentRating(comment, 0, 1);
    }
}


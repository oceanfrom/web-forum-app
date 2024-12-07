package org.example.dao;

import org.example.model.*;
import org.example.transaction.SessionManager;
import org.hibernate.Session;
import java.util.List;

public class CommentDAO {

    public Comment getCommentById(Long commentId) {
        return SessionManager.executeReadOnly(session -> session.get(Comment.class, commentId));
    }

    public void addCommentById(Comment comment) {
         SessionManager.executeInTransactionWithoutReturn(session -> {
            session.save(comment);
        });
    }

    public void deteleCommentById(Long commentId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
           Comment comment = session.load(Comment.class, commentId);
           if(comment != null) {
               session.delete(comment);
           }
        });
    }

    public List<Comment> getCommentsByTopicId(Long topicId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                            "SELECT c FROM Comment c WHERE c.topic.id = :topicId", Comment.class)
                    .setParameter("topicId", topicId)
                    .getResultList();
        });
    }

    public CommentRating existingRating(Session session, Long commentId, Long userId) {
        return session.createQuery(
                        "SELECT c FROM CommentRating c WHERE c.user.id = :userId AND c.comment.id = :commentId ", CommentRating.class)
                .setParameter("userId", userId)
                .setParameter("commentId", commentId).
                uniqueResult();
    }
}


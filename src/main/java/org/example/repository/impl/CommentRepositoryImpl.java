package org.example.repository.impl;

import org.example.model.Comment;
import org.example.model.CommentRating;
import org.example.repository.CommentRepository;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    @Override
    public void deleteCommentById(Long commentId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Comment comment = session.load(Comment.class, commentId);
            if (comment != null) {
                session.delete(comment);
            }
        });
    }

    @Override
    public void saveComment(Comment comment) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            session.save(comment);
        });
    }


    @Override
    public Comment getCommentById(Long commentId) {
        return SessionManager.executeReadOnly(session -> session.get(Comment.class, commentId));
    }

    @Override
    public List<Comment> getCommentsByTopicId(Long topicId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery(
                            "SELECT c FROM Comment c WHERE c.topic.id = :topicId", Comment.class)
                    .setParameter("topicId", topicId)
                    .getResultList();
        });
    }
}

package org.example.repository.impl;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.hibernate.Session;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {

    @Override
    public void deleteCommentById(Session session, Long commentId) {
        Comment comment = session.load(Comment.class, commentId);
        if (comment != null) {
            session.delete(comment);
        }
    }

    @Override
    public void saveComment(Session session, Comment comment) {
        session.save(comment);
    }


    @Override
    public Comment getCommentById(Session session, Long commentId) {
        return session.get(Comment.class, commentId);
    }

    @Override
    public List<Comment> getCommentsByTopicId(Session session, Long topicId) {
        return session.createQuery(
                        "SELECT c FROM Comment c WHERE c.topic.id = :topicId", Comment.class)
                .setParameter("topicId", topicId)
                .getResultList();
    }
}

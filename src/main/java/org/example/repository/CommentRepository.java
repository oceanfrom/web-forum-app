package org.example.repository;

import org.example.model.*;
import org.hibernate.Session;

import java.util.List;

public interface CommentRepository {
    void deleteCommentById(Session session, Long commentId);

    void saveComment(Session session, Comment comment);

    Comment getCommentById(Session session, Long commentId);

    List<Comment> getCommentsByTopicId(Session session, Long topicId);
}


package org.example.repository;

import org.example.model.*;

import java.util.List;

public interface CommentRepository {
    void deleteCommentById(Long commentId);

    void saveComment(Comment comment);

    Comment getCommentById(Long commentId);

    List<Comment> getCommentsByTopicId(Long topicId);
}


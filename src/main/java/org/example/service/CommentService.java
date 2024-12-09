package org.example.service;

import org.example.model.Comment;
import org.example.model.User;
import org.example.model.Topic;

import java.util.List;

public interface CommentService {
    void createComment(String content, User user, Topic topic);
    void deleteCommentById(Long id);
    Comment getCommentById(Long commentId);
    List<Comment> getCommentsByTopicId(Long id);
}

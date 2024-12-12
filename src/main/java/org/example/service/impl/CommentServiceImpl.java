package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.factory.CommentFactory;
import org.example.factory.impl.CommentFactoryImpl;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.repository.impl.CommentRepositoryImpl;
import org.example.service.CommentService;
import org.example.service.NotificationService;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository = new CommentRepositoryImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();
    private final CommentFactory commentFactory = new CommentFactoryImpl();

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.getCommentById(commentId);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteCommentById(id);
    }

    @Override
    public List<Comment> getCommentsByTopicId(Long id) {
        return commentRepository.getCommentsByTopicId(id);
    }

    @Transactional
    @Override
    public void createComment(String content, User user, Topic topic) {
        Comment comment = commentFactory.createComment(content, user, topic);
        commentRepository.saveComment(comment);
        notificationService.createCommentNotification(user, topic, comment);
    }
}

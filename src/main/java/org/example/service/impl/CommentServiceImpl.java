package org.example.service.impl;

import org.example.factory.CommentFactory;
import org.example.factory.impl.CommentFactoryImpl;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.repository.impl.CommentRepositoryImpl;
import org.example.service.CommentService;
import org.example.service.NotificationService;
import org.example.transaction.SessionManager;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository = new CommentRepositoryImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();
    private final CommentFactory commentFactory = new CommentFactoryImpl();

    @Override
    public Comment getCommentById(Long commentId) {
        return SessionManager.executeReadOnly(session -> commentRepository.getCommentById(session, commentId));
    }

    @Override
    public void deleteCommentById(Long id) {
        SessionManager.executeInTransactionWithoutReturn(session -> commentRepository.deleteCommentById(session, id));
    }

    @Override
    public List<Comment> getCommentsByTopicId(Long id) {
        return SessionManager.executeReadOnly(session -> commentRepository.getCommentsByTopicId(session, id));
    }

    @Override
    public void createComment(String content, User user, Topic topic) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Comment comment = commentFactory.createComment(content, user, topic);
            commentRepository.saveComment(session, comment);
            notificationService.createCommentNotification(session, user, topic, comment);
        });
    }
}

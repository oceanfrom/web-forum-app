package org.example.service.impl;

import org.example.factory.NotificationFactory;
import org.example.factory.impl.NotificationFactoryImpl;
import org.example.model.*;
import org.example.repository.NotificationRepository;
import org.example.repository.impl.NotificationRepositoryImpl;
import org.example.service.NotificationService;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository = new NotificationRepositoryImpl();
    private final NotificationFactory notificationFactory = new NotificationFactoryImpl();

    @Override
    public Notification findNotificationForRating(Session session, Topic topic, TopicRating rating) {
        return notificationRepository.findNotificationForRating(session, topic, rating);
    }

    @Override
    public void deleteAllNotifications() {
        SessionManager.executeInTransactionWithoutReturn(session -> notificationRepository.deleteAllNotifications(session));
    }

    @Override
    public void deleteNotificationById(Long id) {
        SessionManager.executeInTransactionWithoutReturn(session -> notificationRepository.deleteNotificationById(session, id));
    }

    @Override
    public void createLikeNotification(User createdBy, Topic topic) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Notification notification = notificationFactory.createLikeNotification(createdBy, topic);
            notificationRepository.saveNotification(session, notification);
        });
    }

    @Override
    public void createCommentNotification(Session session, User createdBy, Topic topic, Comment comment) {
            Notification notification = notificationFactory.createCommentNotification(createdBy, topic, comment);
            notificationRepository.saveNotification(session, notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return SessionManager.executeReadOnly(session -> notificationRepository.getNotificationsByUserId(session, userId));
    }
}

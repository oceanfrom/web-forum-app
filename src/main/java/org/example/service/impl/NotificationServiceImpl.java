package org.example.service.impl;

import org.example.factory.NotificationFactory;
import org.example.factory.impl.NotificationFactoryImpl;
import org.example.model.*;
import org.example.repository.NotificationRepository;
import org.example.repository.impl.NotificationRepositoryImpl;
import org.example.service.NotificationService;
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
        notificationRepository.deleteAllNotifications();
    }

    @Override
    public void deleteNotificationById(Long id) {
        notificationRepository.deleteNotificationById(id);
    }

    @Override
    public void createLikeNotification(User createdBy, Topic topic) {
        Notification notification = notificationFactory.createLikeNotification(createdBy, topic);
        notificationRepository.saveNotification(notification);
    }

    @Override
    public void createCommentNotification(User createdBy, Topic topic, Comment comment) {
        Notification notification = notificationFactory.createCommentNotification(createdBy, topic, comment);
        notificationRepository.saveNotification(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.getNotificationsByUserId(userId);
    }
}

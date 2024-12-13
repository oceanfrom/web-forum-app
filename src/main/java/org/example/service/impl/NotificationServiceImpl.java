package org.example.service.impl;

import org.example.factory.NotificationFactory;
import org.example.factory.impl.NotificationFactoryImpl;
import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;
import org.example.repository.NotificationRepository;
import org.example.repository.impl.NotificationRepositoryImpl;
import org.example.service.NotificationService;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository = new NotificationRepositoryImpl();
    private final NotificationFactory notificationFactory = new NotificationFactoryImpl();

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

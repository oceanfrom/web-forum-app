package org.example.service;

import org.example.model.*;
import org.hibernate.Session;

import java.util.List;

public interface NotificationService {
    Notification findNotificationForRating(Session session, Topic topic, TopicRating rating);

    void deleteAllNotifications();

    void deleteNotificationById(Long id);

    void createLikeNotification(User createdBy, Topic topic);

    void createCommentNotification(Session session, User createdBy, Topic topic, Comment comment);

    List<Notification> getUserNotifications(Long userId);
}

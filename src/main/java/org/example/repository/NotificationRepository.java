package org.example.repository;

import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.hibernate.Session;

import java.util.List;

public interface NotificationRepository {
    Notification findNotificationForRating(Session session, Topic topic, TopicRating rating);

    void deleteAllNotifications(Session session);

    void deleteNotificationById(Session session, Long notificationId);

    void saveNotification(Session session, Notification notification);

    List<Notification> getNotificationsByUserId(Session session, Long userId);
}

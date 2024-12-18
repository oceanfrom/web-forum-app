package org.example.repository;

import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.hibernate.Session;

import java.util.List;

public interface NotificationRepository {
    Notification findNotificationForRating(Session session, Topic topic, TopicRating rating);

    void deleteAllNotifications();

    void deleteNotificationById(Long notificationId);

    void saveNotification(Notification notification);

    List<Notification> getNotificationsByUserId(Long userId);
}

package org.example.repository;

import org.example.model.Notification;

import java.util.List;

public interface NotificationRepository {
    void deleteNotificationById(Long notificationId);

    void saveNotification(Notification notification);

    List<Notification> getNotificationsByUserId(Long userId);
}

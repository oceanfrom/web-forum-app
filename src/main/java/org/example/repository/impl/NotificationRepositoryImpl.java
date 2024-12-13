package org.example.repository.impl;

import org.example.model.Notification;
import org.example.repository.NotificationRepository;
import org.example.transaction.SessionManager;

import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public void deleteAllNotifications() {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            session.createQuery("DELETE FROM Notification").executeUpdate();
        });
    }

    @Override
    public void deleteNotificationById(Long notificationId) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Notification notification = session.get(Notification.class, notificationId);
            if (notification == null)
                return;
            session.delete(notification);
        });
    }

    @Override
    public void saveNotification(Notification notification) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.save(notification));
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return SessionManager.executeReadOnly(session -> {
            return session.createQuery("FROM Notification WHERE topic.createdBy.id = :userId ORDER BY createdAt DESC", Notification.class)
                    .setParameter("userId", userId)
                    .getResultList();
        });
    }
}

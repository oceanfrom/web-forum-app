package org.example.dao;

import org.example.model.Notification;
import org.example.transaction.SessionManager;

import java.util.List;

public class NotificationDAO {
    public void addNotification(Notification notification) {
        SessionManager.executeInTransactionWithoutReturn(session -> session.save(notification));
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return SessionManager.executeInTransaction(session -> {
            return session.createQuery("FROM Notification WHERE topic.createdBy.id = :userId ORDER BY timestamp DESC", Notification.class)
                    .setParameter("userId", userId)
                    .getResultList();
        });
    }
}

package org.example.repository.impl;

import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.repository.NotificationRepository;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public Notification findNotificationForRating(Session session, Topic topic, TopicRating rating) {
        return session.createQuery("FROM Notification n WHERE n.topic = :topic AND n.createdBy = :user", Notification.class)
                .setParameter("topic", topic)
                .setParameter("user", rating.getUser())
                .uniqueResult();
    }

    @Override
    public void deleteAllNotifications(Session session) {
        session.createQuery("DELETE FROM Notification").executeUpdate();
    }

    @Override
    public void deleteNotificationById(Session session, Long notificationId) {
        Notification notification = session.get(Notification.class, notificationId);
        if (notification == null)
            return;
        session.delete(notification);
    }

    @Override
    public void saveNotification(Session session, Notification notification) {
        session.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Session session, Long userId) {
        return session.createQuery("FROM Notification WHERE topic.createdBy.id = :userId ORDER BY createdAt DESC", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

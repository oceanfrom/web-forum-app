package org.example.service;

import org.example.dao.NotificationDAO;
import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;
import java.sql.Timestamp;
import java.util.List;

public class NotificationService {
    private NotificationDAO notificationDAO = new NotificationDAO();

    public void createLikeNotification(User createdBy, Topic topic) {
        Notification notification = new Notification();
        notification.setType(Notification.TYPE_LIKE_TOPIC);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationDAO.addNotification(notification);
    }

    public void createCommentNotification(User createdBy, Topic topic, Comment comment) {
        Notification notification = new Notification();
        notification.setType(Notification.TYPE_COMMENT_TOPIC);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        notification.setComment(comment);
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationDAO.addNotification(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }
}

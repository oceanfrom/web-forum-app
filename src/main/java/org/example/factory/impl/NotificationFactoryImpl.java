package org.example.factory.impl;

import org.example.factory.NotificationFactory;
import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;
import org.example.model.enumiration.NotificationType;

public class NotificationFactoryImpl implements NotificationFactory {
    @Override
    public Notification createLikeNotification(User createdBy, Topic topic) {
        Notification notification = new Notification();
        notification.setType(NotificationType.LIKE);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        return notification;
    }

    @Override
    public Notification createCommentNotification(User createdBy, Topic topic, Comment comment) {
        Notification notification = new Notification();
        notification.setType(NotificationType.COMMENT);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        notification.setComment(comment);
        return notification;
    }
}

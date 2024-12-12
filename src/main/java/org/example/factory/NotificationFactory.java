package org.example.factory;

import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;

public interface NotificationFactory {
    Notification createLikeNotification(User createdBy, Topic topic);

    Notification createCommentNotification(User createdBy, Topic topic, Comment comment);
}

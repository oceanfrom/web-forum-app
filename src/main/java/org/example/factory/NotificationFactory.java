package org.example.factory;

import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;

public class NotificationFactory {
    public Notification createLikeNotification(User createdBy, Topic topic) {
        Notification notification = new Notification();
        notification.setType(Notification.TYPE_LIKE_TOPIC);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        return notification;
    }

    public Notification createCommentNotification(User createdBy, Topic topic, Comment comment) {
        Notification notification = new Notification();
        notification.setType(Notification.TYPE_COMMENT_TOPIC);
        notification.setCreatedBy(createdBy);
        notification.setTopic(topic);
        notification.setComment(comment);
        return notification;
    }
}

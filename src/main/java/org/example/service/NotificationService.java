package org.example.service;

import org.example.model.Comment;
import org.example.model.Notification;
import org.example.model.Topic;
import org.example.model.User;
import java.util.List;

public interface NotificationService {
    public void deleteNotificationById(Long id);
    void createLikeNotification(User createdBy, Topic topic);
    void createCommentNotification(User createdBy, Topic topic, Comment comment);
    List<Notification> getUserNotifications(Long userId);
}

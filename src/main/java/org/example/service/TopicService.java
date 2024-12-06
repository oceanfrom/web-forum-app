package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.TopicDAO;
import org.example.model.Category;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.example.transaction.SessionManager;
import org.hibernate.Session;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class TopicService {
    private TopicDAO topicDAO = new TopicDAO();
    private NotificationService notificationService = new NotificationService();

    public List<Topic> searchByTitle(String title) {
        return topicDAO.searchByTitle(title);
    }

    public List<Topic> getLikedTopicsByUser(Long id) {
        return topicDAO.getLikedTopicsByUser(id);
    }

    public List<Topic> getCreatedTopicsByUser(Long id) {
        return topicDAO.getCreatedTopicsByUser(id);
    }

    public void deleteTopicById(Long id) {
        topicDAO.deleteTopicById(id);
    }

    public Topic getTopicById(Long id) {
        return topicDAO.getTopicById(id);
    }

    public List<Topic> getAllTopicsByLikes() {
        return topicDAO.getAllTopicsByLikes();
    }

    public List<Topic> getAllTopicsByDislikes() {
        return topicDAO.getAllTopicsByDislikes();
    }

    public List<Topic> getAllTopicsByCategoryId(Long id) {
        return topicDAO.getAllTopicsByCategoryId(id);
    }

    public List<Topic> getAllTopics() {
        return topicDAO.getAllTopics();
    }


    public void createTopic(String title, String description, Category category, User user) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(description);
        topic.setCategory(category);
        topic.setCreatedBy(user);
        topic.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        topicDAO.addTopicById(topic);
    }

    public void updateRating(Long topicId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Topic topic = session.get(Topic.class, topicId);
            TopicRating existingRating = topicDAO.findExistingRating(session, topicId, user.getId());
            boolean isNewLike = false;
            if (existingRating != null) {
                isNewLike = processExistingRating(session, topic, existingRating, isLike);
            } else {
                isNewLike = addNewRating(session, topic, user, isLike);
            }

            if (isNewLike && isLike) {
                notificationService.createLikeNotification(user, topic);
            }
        });
    }

    private void changeTopicRating(Topic topic, int like, int dislike) {
        topic.setLikes(topic.getLikes() + like);
        topic.setDislikes(topic.getDislikes() + dislike);
    }

    private boolean processExistingRating(Session session, Topic topic, TopicRating existingRating, boolean isLike) {
        if (existingRating.isLike() == isLike) {
            session.delete(existingRating);
            if (isLike)
                changeTopicRating(topic, -1, 0);
            else
                changeTopicRating(topic, 0, -1);
            return false;
        } else {
            existingRating.setLike(isLike);
            session.update(existingRating);
            if (isLike)
                changeTopicRating(topic, 1, -1);
            else
                changeTopicRating(topic, -1, 1);
            return isLike;
        }
    }

    private boolean addNewRating(Session session, Topic topic, User user, boolean isLike) {
        TopicRating newRating = new TopicRating();
        newRating.setUser(user);
        newRating.setTopic(topic);
        newRating.setLike(isLike);
        session.save(newRating);
        if (isLike)
            changeTopicRating(topic, 1, 0);
        else
            changeTopicRating(topic, 0, 1);
        return true;
    }
}

package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Topic;
import org.example.model.TopicRating;
import org.example.model.User;
import org.example.repository.TopicRatingRepository;
import org.example.repository.impl.TopicRatingRepositoryImpl;
import org.example.service.NotificationService;
import org.example.service.TopicRatingService;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

@Slf4j
public class TopicRatingServiceImpl implements TopicRatingService {
    TopicRatingRepository topicRatingRepository = new TopicRatingRepositoryImpl();
    NotificationService notificationService = new NotificationServiceImpl();

    @Override
    public void updateRating(Long topicId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Topic topic = session.get(Topic.class, topicId);
            TopicRating existingRating = topicRatingRepository.findExistingRating(session, topicId, user.getId());
            boolean isNewLike = false;
            if (existingRating != null) {
                isNewLike = processExistingRating(session, topic, existingRating, isLike);
            } else {
                isNewLike = addNewRating(session, topic, user, isLike);
            }

            if (isNewLike && isLike) {
                notificationService.createLikeNotification(user, topic);
                log.info("NOTIFICATION LIKE BY USER {} IN TOPIC {} SEND", user, topic);
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

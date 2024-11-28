package org.example.service;

import org.example.model.Topic;
import org.example.utils.impl.SessionManager;
import org.hibernate.query.Query;

import java.util.List;

public class TopicService {
    public List<Topic> getAllTopics() {
        return SessionManager.executeInTransaction(session -> {
            Query<Topic> query = session.createQuery(
                    "SELECT t FROM Topic t " +
                            "LEFT JOIN FETCH t.createdBy ", Topic.class);
            List<Topic> topics = query.getResultList();
            return topics;
        });
    }
}

package org.example.service.actions.impl.sort;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.actions.SortingHandler;
import org.example.model.Topic;
import org.example.service.TopicService;
import org.example.service.impl.TopicServiceImpl;
import java.util.List;

public class AllTopicsHandlerImpl implements SortingHandler {
    private TopicService topicService = new TopicServiceImpl();

    @Override
    public List<Topic> getTopics(HttpServletRequest req, HttpServletResponse resp) {
        return topicService.getAllTopics();
    }
}

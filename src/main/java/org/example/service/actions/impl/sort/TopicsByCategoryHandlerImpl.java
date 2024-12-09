package org.example.service.actions.impl.sort;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.actions.SortingHandler;
import org.example.model.Topic;
import org.example.service.TopicService;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;
import java.util.List;

public class TopicsByCategoryHandlerImpl implements SortingHandler {
    private TopicService topicService = new TopicServiceImpl();
    @Override
    public List<Topic> getTopics(HttpServletRequest req, HttpServletResponse resp) throws IdParserUtils.InvalidIdException {
        Long categoryId = IdParserUtils.parseId(req.getParameter("category"));
        return topicService.getAllTopicsByCategoryId(categoryId);
    }
}

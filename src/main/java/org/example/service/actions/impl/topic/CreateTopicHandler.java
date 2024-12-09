package org.example.service.actions.impl.topic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.Category;
import org.example.model.User;
import org.example.service.CategoryService;
import org.example.service.TopicService;
import org.example.service.impl.CategoryServiceImpl;
import org.example.service.impl.TopicServiceImpl;
import org.example.utils.IdParserUtils;
import java.io.IOException;

@Slf4j
public class CreateTopicHandler implements ActionHandler {
    private final TopicService topicService = new TopicServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            log.warn("Missing or invalid input: title, description, or category ID is empty.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return;
        }
        Long categoryId = IdParserUtils.parseId(req.getParameter("categoryIdStr"));
        Category category = categoryService.getCategoryById(categoryId);
        topicService.createTopic(title, description, category, currentUser);
        log.info("Successfully created topic with title: {} in category {}.", title, category.getName());
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req){
        return "/forum";
    }
}

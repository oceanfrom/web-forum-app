package org.example.service.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Topic;
import org.example.utils.IdParserUtils;

import java.io.IOException;
import java.util.List;

public interface SortingHandler {
    List<Topic> getTopics(HttpServletRequest req, HttpServletResponse resp) throws IOException, IdParserUtils.InvalidIdException;

    default String getRedirectUrl(HttpServletRequest req) {
        return null;
    }
}

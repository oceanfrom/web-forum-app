package org.example.service.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.utils.IdParserUtils;

import java.io.IOException;

// for topic & comment
public interface ActionHandler {
    void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, IdParserUtils.InvalidIdException;

    default String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        return null;
    }
}

package org.example.service.actions.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.actions.ActionHandler;
import org.example.service.actions.ActionManager;
import org.example.utils.IdParserUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class ActionManagerImpl implements ActionManager {
    private final Map<String, ActionHandler> actions;

    public ActionManagerImpl(Map<String, ActionHandler> actions) {
        this.actions = actions;
    }

    public void handleAction(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        String action = req.getParameter("action");
        try {
            ActionHandler handler = actions.get(action);
            if (handler != null) {
                handler.execute(req, resp, currentUser);
                resp.sendRedirect(handler.getRedirectUrl(req));
            } else {
                log.warn("Unknown action: {}", action);
                handleError(resp);
            }
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            handleError(resp);
        } catch (SecurityException e) {
            log.warn("Unauthorized action attempted: {}", e.getMessage());
            handleError(resp);
        }
    }

    private void handleError(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/error");
    }
}

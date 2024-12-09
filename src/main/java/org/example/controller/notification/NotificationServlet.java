package org.example.controller.notification;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Notification;
import org.example.model.User;
import org.example.service.NotificationService;
import org.example.service.impl.NotificationServiceImpl;
import org.example.controller.BaseServlet;
import org.example.utils.context.ContextGeneration;
import org.example.utils.context.impl.ContextGenerationImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.config.ThymeleafConfig;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/notifications")
public class NotificationServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    private NotificationService notificationService;
    private ContextGeneration contextGeneration;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        notificationService = new NotificationServiceImpl();
        contextGeneration = new ContextGenerationImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = getCurrentUser(req);
        try {
            ensureUserLoggedIn(resp,currentUser);
        } catch (IOException e) {
            return;
        }

        List<Notification> notifications = notificationService.getUserNotifications(currentUser.getId());
        Context context = contextGeneration.createContext("notifications", notifications);
        templateEngine.process("notifications", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        Long notificationId = parseId(resp, req.getParameter("notificationId"));
        if (action.equals("notification-delete")) {
            notificationService.deleteNotificationById(notificationId);
            handleRedirect(resp, "/notifications");
        }
    }
}
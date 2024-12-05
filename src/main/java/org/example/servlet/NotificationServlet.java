package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Notification;
import org.example.model.User;
import org.example.service.NotificationService;
import org.example.utils.IdParserUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.config.ThymeleafConfig;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private NotificationService notificationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        notificationService = new NotificationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Notification> notifications = notificationService.getUserNotifications(currentUser.getId());
        Context context = createContextVal(notifications);
        templateEngine.process("notifications", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        Long notificationId = null;
        try {
            notificationId = IdParserUtils.parseId(req.getParameter("notificationId"));
        } catch (IdParserUtils.InvalidIdException e) {
            log.error("Error parsing ID", e);
            resp.sendRedirect(req.getContextPath() + "/error");
        }

        if (action.equals("notification-delete")) {
            notificationService.deleteNotificationById(notificationId);
            resp.sendRedirect(req.getContextPath() + "/notifications");
        }
    }

    public Context createContextVal(List<Notification> notifications) {
        Context context = new Context();
        context.setVariable("notifications", notifications);
        return context;
    }
}
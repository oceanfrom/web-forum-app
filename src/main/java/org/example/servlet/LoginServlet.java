package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.UserService;
import org.example.utils.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    TemplateEngine templateEngine;
    UserService userService;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession().getAttribute("loggedIn") != null && (boolean) req.getSession().getAttribute("loggedIn")) {
            resp.sendRedirect("/forum");
            return;
        }

        Context context = new Context();
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("loggedIn", true);
            resp.sendRedirect("/forum");
        } else {
            resp.sendRedirect("/login?error=Invalid+username+or+password");
        }

    }
}

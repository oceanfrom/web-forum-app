package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.dao.UserDAO;
import org.example.config.ThymeleafConfig;
import org.example.validator.EmailValidator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserDAO userDAO;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            log.info("User already logged in, redirecting to forum.");
            resp.sendRedirect("/forum");
        }


        Context context = new Context();
        templateEngine.process("login", context, resp.getWriter());
        log.info("Login page rendered successfully.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (!EmailValidator.isValidEmail(email)) {
            log.warn("Invalid email format: {}", email);
            resp.sendRedirect("/login?error=Invalid+email");
            return;
        }

        if (password == null || password.length() < 6) {
            log.warn("Password is too short (less than 6 characters).");
            resp.sendRedirect("/login?error=Password+must+be+at+least+6+characters");
            return;
        }

        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            log.info("User {} logged in successfully.", email);
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("loggedIn", true);
            resp.sendRedirect("/forum");
        } else {
            log.warn("Invalid username or password for email: {}", email);
            resp.sendRedirect("/login?error=Invalid+username+or+password");
        }
    }
}

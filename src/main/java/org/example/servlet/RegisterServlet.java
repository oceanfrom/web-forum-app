package org.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ThymeleafConfig;
import org.example.model.User;
import org.example.service.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Slf4j
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafConfig.getTemplateEngine();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        templateEngine.process("register", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        String errorMessage = userService.registerUser(user);

        if (errorMessage != null) {
            log.warn("Registration failed: {}", errorMessage);
            Context context = createContextErrorVal(errorMessage);
            templateEngine.process("register", context, resp.getWriter());
            return;
        }
        log.info("User {} registered successfully.", email);
        resp.sendRedirect("/login");
    }

    private Context createContextErrorVal(String errorMessage) {
        Context context = new Context();
        context.setVariable("errorMessage", errorMessage);
        return context;
    }
}

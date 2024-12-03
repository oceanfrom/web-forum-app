package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.ThymeleafConfig;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;
import java.util.Map;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init(){
        templateEngine = ThymeleafConfig.getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = ThymeleafContextUtils.createContext(Map.of());
        templateEngine.process("error", context, resp.getWriter());
    }
}

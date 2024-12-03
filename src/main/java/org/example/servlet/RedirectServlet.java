package org.example.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.ThymeleafConfig;
import org.example.utils.ThymeleafContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class RedirectServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Context context = ThymeleafContextUtils.createContext(Map.of());
        templateEngine.process("welcomepage", context, response.getWriter());
        log.info("Successfully rendered welcome page.");
    }
}

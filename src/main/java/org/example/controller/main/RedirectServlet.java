package org.example.controller.main;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class RedirectServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() {
        templateEngine = ThymeleafConfig.getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Context context = new Context();
        templateEngine.process("welcomepage", context, response.getWriter());
        log.info("Successfully rendered welcome page.");
    }
}

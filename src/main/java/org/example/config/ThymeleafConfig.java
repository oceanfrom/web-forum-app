package org.example.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafConfig {

    private static volatile TemplateEngine templateEngine;

    public static TemplateEngine getTemplateEngine() {
        if (templateEngine == null) {
            synchronized (ThymeleafConfig.class) {
                if (templateEngine == null) {
                    templateEngine = createTemplateEngine();
                }
            }
        }
        return templateEngine;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/views/");
        templateResolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}

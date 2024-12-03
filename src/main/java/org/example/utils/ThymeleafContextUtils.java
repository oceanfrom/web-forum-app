package org.example.utils;

import org.thymeleaf.context.Context;

import java.util.Map;

public class ThymeleafContextUtils {
    public static Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }
}

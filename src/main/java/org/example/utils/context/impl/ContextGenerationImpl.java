package org.example.utils.context.impl;

import org.example.utils.context.ContextGeneration;
import org.thymeleaf.context.Context;

public class ContextGenerationImpl implements ContextGeneration {

    @Override
    public Context createContext(Object... vars) {
        Context context = new Context();
        for (int i = 0; i < vars.length; i += 2) {
            String key = (String) vars[i];
            Object value = vars[i + 1];
            context.setVariable(key, value);
        }

        return context;
    }
}

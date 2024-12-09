package org.example.utils.context;

import org.thymeleaf.context.Context;

public interface ContextGeneration {
    Context createContext(Object... vars);
}

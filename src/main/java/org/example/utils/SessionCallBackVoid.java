package org.example.utils;

import org.hibernate.Session;

public interface SessionCallBackVoid {
    void doInSession(Session session);
}

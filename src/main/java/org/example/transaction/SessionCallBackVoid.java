package org.example.transaction;

import org.hibernate.Session;

public interface SessionCallBackVoid {
    void doInSession(Session session);
}

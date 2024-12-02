package org.example.transaction;

import org.hibernate.Session;

public interface SessionCallback<T> {
    T doInSession(Session session);
}

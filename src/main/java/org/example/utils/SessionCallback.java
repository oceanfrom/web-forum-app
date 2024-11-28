package org.example.utils;

import org.hibernate.Session;

public interface SessionCallback<T> {
    T doInSession(Session session);
}

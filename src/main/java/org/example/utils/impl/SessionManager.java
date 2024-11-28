package org.example.utils.impl;

import org.example.utils.HibernateUtil;
import org.example.utils.SessionCallBackVoid;
import org.example.utils.SessionCallback;
import org.hibernate.Session;

public class SessionManager {

    public static <T> T executeInTransaction(SessionCallback<T> callback) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        T result = null;

        try {
            result = callback.doInSession(session);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public static void executeInTransactionWithoutReturn(SessionCallBackVoid callback) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            callback.doInSession(session);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

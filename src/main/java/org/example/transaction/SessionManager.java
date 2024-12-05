package org.example.transaction;

import org.example.config.HibernateConfig;
import org.hibernate.Session;

public class SessionManager {

    public static <T> T executeReadOnly(SessionCallback<T> callback) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        T result = null;
        try {
            session.setDefaultReadOnly(true);
            result = callback.doInSession(session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public static void executeInTransactionWithoutReturn(SessionCallBackVoid callback) {
        Session session = HibernateConfig.getSessionFactory().openSession();
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

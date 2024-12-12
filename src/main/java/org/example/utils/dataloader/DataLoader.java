package org.example.utils.dataloader;

import org.example.transaction.SessionManager;

public class DataLoader {
    private DataLoader() {}
    public static void loadTestData() {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            session.createNativeQuery(InitSQL.INSERT_USERS).executeUpdate();
            session.createNativeQuery(InitSQL.INSERT_CATEGORIES).executeUpdate();
            session.createNativeQuery(InitSQL.INSERT_TOPICS).executeUpdate();
            session.createNativeQuery(InitSQL.INSERT_COMMENTS).executeUpdate();
        });
    }
}

package org.example.config;

import lombok.Getter;
import org.example.model.*;
import org.example.model.enumiration.Role;
import org.example.utils.dataloader.DataLoader;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateConfig {
    @Getter
    private static SessionFactory sessionFactory;
    static {
        try {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USERNAME");
            String dbPassword = System.getenv("DB_PASSWORD");
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            if (dbUrl != null) {
                configuration.setProperty("hibernate.connection.url", dbUrl);
            }
            if (dbUser != null) {
                configuration.setProperty("hibernate.connection.username", dbUser);
            }
            if (dbPassword != null) {
                configuration.setProperty("hibernate.connection.password", dbPassword);
            }
            configuration.addAnnotatedClass(User.class)
                    .addAnnotatedClass(Topic.class)
                    .addAnnotatedClass(TopicRating.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(CommentRating.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Notification.class)
                    .addAnnotatedClass(Role.class);

            sessionFactory = configuration.buildSessionFactory();
            DataLoader.loadTestData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
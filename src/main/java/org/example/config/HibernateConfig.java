package org.example.config;

import lombok.Getter;
import org.example.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    @Getter
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Topic.class)
                    .addAnnotatedClass(TopicRating.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(CommentRating.class)
                    .addAnnotatedClass(Category.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

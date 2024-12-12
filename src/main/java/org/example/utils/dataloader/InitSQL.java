package org.example.utils.dataloader;

public class InitSQL {
    public static final String INSERT_USERS =
            "INSERT INTO users (username, password, email, registration_date, role) VALUES " +
            "('John Doe', '$2a$10$paqlDkAwuTRA9vXbdze7geQ49fN3FWGh6JmPBFFq1scRhKf6V9.S2', 'john.doe@example.com', CURRENT_TIMESTAMP, 'USER'), " +
            "('Alice Smith', '$2a$10$8dq/TjVYFvHCCjJ7B/NGYOxg5UvqbjY6.OVS0O2hHeMd1JF.MwTMi', 'alice.smith@example.com', CURRENT_TIMESTAMP, 'ADMIN'), " +
            "('Bob Jones', '$2a$10$eysDJ33K23qvZ/WBh/HzDeDNPuO4CZt4210.wODSrCiC7xR1WcYDy', 'bob.jones@example.com', CURRENT_TIMESTAMP, 'USER')";

    public static final String INSERT_CATEGORIES =
            "INSERT INTO categories (name) VALUES " +
                    "('Code Help'), " +
                    "('Off-Topic & Life Discussions'), " +
                    "('Technology News & Trends'), " +
                    "('Project Showcase & Portfolio'), " +
                    "('Learning & Tutorials')";

    public static final String INSERT_TOPICS =
            "INSERT INTO topics (title, content, category_id, created_by, created_at, likes, dislikes) VALUES " +
            "('Spring Framework Basics', 'A beginner''s guide to Spring Framework in Java.', 1, 1, CURRENT_TIMESTAMP, 0, 0), " +
            "('Introduction to Quantum Computing', 'A simple introduction to the concepts of quantum computing.', 3, 2, CURRENT_TIMESTAMP, 0, 0), " +
            "('Why the Future is Electric Cars', 'Discussion on the impact of electric cars on the environment and future of transportation.', 2, 1, CURRENT_TIMESTAMP, 0, 0), " +
            "('The Rise of Artificial Intelligence', 'Exploring how AI is transforming industries and everyday life.', 3, 2, CURRENT_TIMESTAMP, 0, 0), " +
            "('My Portfolio: Web Design Projects', 'A showcase of web design projects and experiments I''ve worked on.', 4, 1, CURRENT_TIMESTAMP, 21, 3), " +
            "('Learning Python for Data Science', 'Python basics for beginners aiming to get into data science.', 5, 3, CURRENT_TIMESTAMP, 3, 52), " +
            "('How to Build a Personal Website', 'Step-by-step guide to creating a personal website using HTML, CSS, and JavaScript.', 5, 1, CURRENT_TIMESTAMP, 102, 39), " +
            "('React JS for Beginners', 'Introduction to React JS and its component-based architecture.', 1, 2, CURRENT_TIMESTAMP, 341, 12), " +
            "('Tech Innovations in 2024', 'What are the latest tech trends and innovations that will shape 2024?', 3, 1, CURRENT_TIMESTAMP, 23, 1)";

    public static final String INSERT_COMMENTS =
            "INSERT INTO comments (content, created_by, topic_id, created_at, likes, dislikes) VALUES " +
                    "('Great topic! Very useful for beginners.', 1, 1, CURRENT_TIMESTAMP, 5, 0), " +
                    "('Hibernate is an amazing framework!', 2, 2, CURRENT_TIMESTAMP, 3, 0), " +
                    "('I love PostgreSQL!', 3, 3, CURRENT_TIMESTAMP, 2, 1)";
}

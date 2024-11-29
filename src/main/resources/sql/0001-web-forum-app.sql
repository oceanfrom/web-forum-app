CREATE TABLE users
(
    id                SERIAL PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    email             VARCHAR(100) NOT NULL UNIQUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE topics
(
    id         SERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    category   VARCHAR(50)  NOT NULL,
    created_by INT REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    likes      INT       DEFAULT 0,
    dislikes   INT       DEFAULT 0
);

INSERT INTO users (username, password, email, registration_date)
VALUES ('john_doe', 'password123', 'john.doe@example.com', CURRENT_TIMESTAMP),
       ('alice_smith', 'password456', 'alice.smith@example.com', CURRENT_TIMESTAMP),
       ('bob_jones', 'password789', 'bob.jones@example.com', CURRENT_TIMESTAMP);

INSERT INTO topics (title, content, category, created_by, created_at, likes, dislikes)
VALUES ('Java Basics', 'This is a beginner topic about Java programming.', 'Programming', 1, CURRENT_TIMESTAMP, 10, 2),
       ('Hibernate Introduction', 'Learn the basics of the Hibernate ORM framework.', 'Programming', 2,
        CURRENT_TIMESTAMP, 5, 1),
       ('PostgreSQL Overview', 'PostgreSQL is a powerful relational database system.', 'Database', 3, CURRENT_TIMESTAMP,
        3, 0);

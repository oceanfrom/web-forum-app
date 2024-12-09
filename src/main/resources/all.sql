CREATE TABLE users
(
    id                SERIAL PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL,
    password          VARCHAR(100) NOT NULL,
    email             VARCHAR(100) NOT NULL UNIQUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role              VARCHAR(50)  NOT NULL
);

INSERT INTO users (username, password, email, registration_date, role)
VALUES ('John Doe', '$2a$10$paqlDkAwuTRA9vXbdze7geQ49fN3FWGh6JmPBFFq1scRhKf6V9.S2', 'john.doe@example.com',
        CURRENT_TIMESTAMP, 'USER'),
       ('Alice Smith', '$2a$10$8dq/TjVYFvHCCjJ7B/NGYOxg5UvqbjY6.OVS0O2hHeMd1JF.MwTMi', 'alice.smith@example.com',
        CURRENT_TIMESTAMP, 'ADMIN'),
       ('Bob Jones', '$2a$10$eysDJ33K23qvZ/WBh/HzDeDNPuO4CZt4210.wODSrCiC7xR1WcYDy', 'bob.jones@example.com',
        CURRENT_TIMESTAMP, 'USER');


CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE topics
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    content     TEXT         NOT NULL,
    category_id INT          REFERENCES categories (id) ON DELETE SET NULL,
    created_by  INT REFERENCES users (id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    likes       INT       DEFAULT 0,
    dislikes    INT       DEFAULT 0
);

CREATE TABLE topic_rating
(
    id           SERIAL PRIMARY KEY,
    user_id      INT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    topic_id     INT NOT NULL REFERENCES topics (id) ON DELETE CASCADE,
    like_dislike BOOLEAN,
    UNIQUE (user_id, topic_id)
);

CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    content    TEXT NOT NULL,
    created_by INT REFERENCES users (id) ON DELETE CASCADE,
    topic_id   INT REFERENCES topics (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    likes      INT       DEFAULT 0,
    dislikes   INT       DEFAULT 0
);

CREATE TABLE comment_rating
(
    id           SERIAL PRIMARY KEY,
    user_id      INT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    comment_id   INT NOT NULL REFERENCES comments (id) ON DELETE CASCADE,
    like_dislike BOOLEAN,
    UNIQUE (user_id, comment_id)
);

CREATE TABLE notifications
(
    id         SERIAL PRIMARY KEY,
    type       VARCHAR(50) NOT NULL,
    created_by BIGINT      NOT NULL,
    topic_id   BIGINT      NOT NULL,
    comment_id BIGINT,
    created_at TIMESTAMP   NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE
);

INSERT INTO categories (name)
VALUES ('Code Help'),
       ('Off-Topic & Life Discussions'),
       ('Technology News & Trends'),
       ('Project Showcase & Portfolio'),
       ('Learning & Tutorials');

INSERT INTO topics (title, content, category_id, created_by, created_at, likes, dislikes)
VALUES ('Spring Framework Basics', 'A beginner''s guide to Spring Framework in Java.', 1, 1, CURRENT_TIMESTAMP, 0, 0),
       ('Introduction to Quantum Computing', 'A simple introduction to the concepts of quantum computing.', 3, 2,
        CURRENT_TIMESTAMP, 0, 0),
       ('Why the Future is Electric Cars',
        'Discussion on the impact of electric cars on the environment and future of transportation.', 2, 1,
        CURRENT_TIMESTAMP, 0, 0),
       ('The Rise of Artificial Intelligence', 'Exploring how AI is transforming industries and everyday life.', 3, 2,
        CURRENT_TIMESTAMP, 0, 0),
       ('My Portfolio: Web Design Projects', 'A showcase of web design projects and experiments I''ve worked on.', 4, 1,
        CURRENT_TIMESTAMP, 21, 3),
       ('Learning Python for Data Science', 'Python basics for beginners aiming to get into data science.', 5, 3,
        CURRENT_TIMESTAMP, 3, 52),
       ('How to Build a Personal Website',
        'Step-by-step guide to creating a personal website using HTML, CSS, and JavaScript.', 5, 1, CURRENT_TIMESTAMP,
        102, 39),
       ('React JS for Beginners', 'Introduction to React JS and its component-based architecture.', 1, 2,
        CURRENT_TIMESTAMP, 341, 12),
       ('Tech Innovations in 2024', 'What are the latest tech trends and innovations that will shape 2024?', 3, 1,
        CURRENT_TIMESTAMP, 23, 1);

INSERT INTO comments (content, created_by, topic_id, created_at, likes, dislikes)
VALUES ('Great topic! Very useful for beginners.', 1, 1, CURRENT_TIMESTAMP, 5, 0),
       ('Hibernate is an amazing framework!', 2, 2, CURRENT_TIMESTAMP, 3, 0),
       ('I love PostgreSQL!', 3, 3, CURRENT_TIMESTAMP, 2, 1);


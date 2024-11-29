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



INSERT INTO comments (content, created_by, topic_id, created_at, likes, dislikes)
VALUES ('Great topic! Very useful for beginners.', 1, 1, CURRENT_TIMESTAMP, 5, 0),
       ('Hibernate is an amazing framework!', 2, 2, CURRENT_TIMESTAMP, 3, 0),
       ('I love PostgreSQL!', 3, 3, CURRENT_TIMESTAMP, 2, 1);
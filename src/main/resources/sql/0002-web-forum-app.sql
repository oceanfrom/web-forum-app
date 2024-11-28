CREATE TABLE topic_rating (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL  REFERENCES users(id) ON DELETE CASCADE,
                        topic_id INT NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
                        like_dislike BOOLEAN,
                        UNIQUE(user_id, topic_id)
);

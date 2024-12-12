package org.example.factory;

import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;

public interface CommentFactory {
    Comment createComment(String content, User user, Topic topic);
}

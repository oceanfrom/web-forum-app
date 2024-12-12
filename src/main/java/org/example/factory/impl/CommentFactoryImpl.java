package org.example.factory.impl;

import org.example.factory.CommentFactory;
import org.example.model.Comment;
import org.example.model.Topic;
import org.example.model.User;

public class CommentFactoryImpl implements CommentFactory {
    public Comment createComment(String content, User user, Topic topic) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTopic(topic);
        comment.setCreatedBy(user);
        return comment;
    }
}

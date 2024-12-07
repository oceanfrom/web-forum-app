package org.example.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.CommentDAO;
import org.example.model.Comment;
import org.example.model.CommentRating;
import org.example.model.Topic;
import org.example.model.User;
import org.example.transaction.SessionManager;
import org.hibernate.Session;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class CommentService {
    private CommentDAO commentDAO = new CommentDAO();
    private NotificationService notificationService = new NotificationService();

    public Comment getCommentById(Long commentId) {
        return commentDAO.getCommentById(commentId);
    }

    public void deteleCommentById(Long id) {
        commentDAO.deteleCommentById(id);
    }

    public List<Comment> getCommentsByTopicId(Long id) {
        return commentDAO.getCommentsByTopicId(id);
    }

    @Transactional
    public void createComment(String content, User user, Topic topic) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTopic(topic);
        comment.setCreatedBy(user);
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        commentDAO.addCommentById(comment);
        notificationService.createCommentNotification(user, topic, comment);
    }

    public void updateRating(Long commentId, User user, boolean isLike) {
        SessionManager.executeInTransactionWithoutReturn(session -> {
            Comment comment = session.get(Comment.class, commentId);
            CommentRating existingRating = commentDAO.existingRating(session, commentId, user.getId());
            if (existingRating != null)
                processExistingRating(session, comment, existingRating, isLike);
            else
                addNewRating(session, comment, user, isLike);
        });
    }

    private void changeCommentRating(Comment comment, int like, int dislike) {
        comment.setLikes(comment.getLikes() + like);
        comment.setDislikes(comment.getDislikes() + dislike);
    }

    private void processExistingRating(Session session, Comment comment, CommentRating existingRating, boolean isLike) {
        if (existingRating.isLike() == isLike) {
            session.delete(existingRating);
            if (isLike)
                changeCommentRating(comment, -1, 0);
            else
                changeCommentRating(comment, 0, -1);
        } else {
            existingRating.setLike(isLike);
            session.update(existingRating);
            if (isLike)
                changeCommentRating(comment, 1, -1);
            else
                changeCommentRating(comment, -1, 1);
        }
    }

    private void addNewRating(Session session, Comment comment, User user, boolean isLike) {
        CommentRating newRating = new CommentRating();
        newRating.setUser(user);
        newRating.setComment(comment);
        newRating.setLike(isLike);
        session.save(newRating);
        if (isLike)
            changeCommentRating(comment, 1, 0);
        else
            changeCommentRating(comment, 0, 1);
    }
}

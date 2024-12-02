package service;

import org.example.dao.CommentDAO;
import org.example.model.Comment;
import org.example.model.CommentRating;
import org.example.model.User;
import org.example.transaction.SessionManager;
import org.hibernate.Session;

public class CommentService {
    private CommentDAO commentDAO = new CommentDAO();

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

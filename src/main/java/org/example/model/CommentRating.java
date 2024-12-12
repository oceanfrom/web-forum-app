package org.example.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comment_rating")
public class CommentRating{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(name = "like_dislike")
    private boolean like;

}

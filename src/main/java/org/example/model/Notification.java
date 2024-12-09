package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    public static final String TYPE_LIKE_TOPIC = "LIKE_TOPIC";
    public static final String TYPE_COMMENT_TOPIC = "COMMENT_TOPIC";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    private User createdBy;
    @ManyToOne
    @JoinColumn(name = "topic_id",nullable = false)
    private Topic topic;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}

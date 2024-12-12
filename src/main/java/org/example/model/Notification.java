package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.model.enumiration.NotificationType;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne()
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}

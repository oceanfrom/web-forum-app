package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private String category;
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    private Timestamp createdAt;
    private int likes;
    private int dislikes;
}

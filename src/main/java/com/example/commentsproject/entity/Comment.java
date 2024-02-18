package com.example.commentsproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String username;

    @Column(name = "news_id", nullable = false)
    private Long newsId;

    @PrePersist
    public void prePersist() {
        if (this.time == null) {
            LocalDateTime now = LocalDateTime.now();
            this.time = now;
            this.updateTime = now;
        }
        if (username == null) {
            this.username = "anonymous";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}

package by.clevertec.commentsproject.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * Сущность "Комментарий".
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String username;

    /**
     * Новость, к которой относится комментарий.
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    /**
     * Метод, выполняемый перед сохранением комментария. Устанавливает время создания и обновления, а также имя пользователя.
     */
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

    /**
     * Метод, выполняемый перед обновлением комментария. Обновляет время обновления.
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}

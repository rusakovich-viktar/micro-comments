package by.clevertec.commentsproject.repository;

import by.clevertec.commentsproject.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    void deleteByNewsId(Long newsId);

}


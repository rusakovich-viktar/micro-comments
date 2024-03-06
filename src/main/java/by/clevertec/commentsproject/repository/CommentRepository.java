package by.clevertec.commentsproject.repository;

import by.clevertec.commentsproject.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностями "Комментарий".
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Находит все комментарии к новости по идентификатору новости.
     *
     * @param newsId   идентификатор новости
     * @param pageable параметры пагинации
     * @return страница с комментариями
     */
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    /**
     * Удаляет все комментарии к новости по идентификатору новости.
     *
     * @param newsId идентификатор новости
     */
    void deleteByNewsId(Long newsId);

}

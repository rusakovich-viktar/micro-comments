package by.clevertec.commentsproject.controller;

import static by.clevertec.commentsproject.util.Constant.BaseApi.COMMENTS;
import static by.clevertec.commentsproject.util.Constant.BaseApi.ID;
import static by.clevertec.commentsproject.util.Constant.BaseApi.NEWS_NEWS_ID;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления комментариев.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENTS)
public class CommentController {

    private final CommentService commentService;

    /**
     * Создает комментарий к новости.
     *
     * @param newsId            идентификатор новости
     * @param commentRequestDto DTO запроса на создание комментария
     * @return созданный комментарий
     */
    @PostMapping(NEWS_NEWS_ID)
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long newsId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(
                commentService.createComment(newsId, commentRequestDto),
                HttpStatus.CREATED);
    }

    /**
     * Получает комментарий по идентификатору.
     *
     * @param id идентификатор комментария
     * @return комментарий
     */
    @GetMapping(ID)
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    /**
     * Обновляет комментарий.
     *
     * @param id                идентификатор комментария
     * @param commentRequestDto DTO запроса на обновление комментария
     * @return обновленный комментарий
     */
    @PutMapping(ID)
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(id, commentRequestDto));
    }

    /**
     * Удаляет комментарий.
     *
     * @param id идентификатор комментария
     * @return HTTP статус 204 (No Content), если комментарий успешно удален
     */
    @DeleteMapping(ID)
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает все комментарии.
     *
     * @param pageable параметры пагинации
     * @return страница с комментариями
     */
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getAllComment(Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllComment(pageable));
    }

    /**
     * Не используется напрямую, только как эндпоинт для Feign Client сервиса новостей.
     * Удаляет все комментарии к новости.
     *
     * @param newsId идентификатор новости
     * @return HTTP статус 204 (No Content), если комментарии успешно удалены
     */
    @DeleteMapping(NEWS_NEWS_ID)
    public ResponseEntity<Void> deleteCommentsByNewsId(@PathVariable Long newsId) {
        commentService.deleteCommentsByNewsId(newsId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Не используется напрямую, только как эндпоинт для Feign Client сервиса новостей.
     * Получает все комментарии к новости.
     *
     * @param newsId   идентификатор новости
     * @param pageable параметры пагинации
     * @return страница с комментариями к новости
     */
    @GetMapping(NEWS_NEWS_ID)
    ResponseEntity<Page<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

}

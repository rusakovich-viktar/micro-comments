package by.clevertec.commentsproject.service.impl;

import static by.clevertec.commentsproject.util.Constant.Atrubutes.COMMENT;

import by.clevertec.commentsproject.client.NewsClient;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.mapper.CommentMapper;
import by.clevertec.commentsproject.repository.CommentRepository;
import by.clevertec.commentsproject.service.CommentService;
import by.clevertec.exception.EntityNotFoundExceptionCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для управления комментариями.
 */
@Service
@EnableCaching
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsClient newsClient;

    /**
     * Создает комментарий к новости.
     *
     * @param newsId            идентификатор новости
     * @param commentRequestDto DTO запроса на создание комментария
     * @return созданный комментарий
     */
    @Override
    @Transactional
    @CachePut(value = COMMENT, key = "#result.id")
    public CommentResponseDto createComment(Long newsId, CommentRequestDto commentRequestDto) {

        ResponseEntity<NewsResponseDto> response = newsClient.getNewsById(newsId);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw EntityNotFoundExceptionCustom.of(NewsResponseDto.class, newsId);
        }

        NewsResponseDto newsResponseDto = response.getBody();

        Comment comment = commentMapper.toEntity(commentRequestDto);

        News news = new News();
        news.setId(newsResponseDto.getId());

        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    /**
     * Получает комментарий по идентификатору.
     *
     * @param id идентификатор комментария
     * @return комментарий
     */
    @Override
    @Cacheable(value = COMMENT)
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() ->
                        EntityNotFoundExceptionCustom.of(Comment.class, id));
        return commentMapper.toDto(comment);
    }

    /**
     * Обновляет комментарий.
     *
     * @param id                идентификатор комментария
     * @param commentRequestDto DTO запроса на обновление комментария
     * @return обновленный комментарий
     */
    @Override
    @Transactional
    @CachePut(value = COMMENT, key = "#id")
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(Comment.class, id));

        commentMapper.updateFromDto(commentRequestDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    /**
     * Удаляет комментарий.
     *
     * @param id идентификатор комментария
     */
    @Override
    @Transactional
    @CacheEvict(value = COMMENT, key = "#id")
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(Comment.class, id));
        commentRepository.delete(comment);
    }

    /**
     * Получает все комментарии.
     *
     * @param pageable параметры пагинации
     * @return страница с комментариями
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getAllComment(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(commentMapper::toDto);
    }

    /**
     * Удаляет все комментарии к новости.
     *
     * @param newsId идентификатор новости
     */
    @Override
    @Transactional
    public void deleteCommentsByNewsId(Long newsId) {
        commentRepository.deleteByNewsId(newsId);
    }

    /**
     * Получает все комментарии к новости.
     *
     * @param newsId   идентификатор новости
     * @param pageable параметры пагинации
     * @return страница с комментариями к новости
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable) {
        return commentRepository.findByNewsId(newsId, pageable)
                .map(commentMapper::toDto);

    }

}

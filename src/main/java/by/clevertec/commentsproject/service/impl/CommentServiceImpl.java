package by.clevertec.commentsproject.service.impl;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import by.clevertec.commentsproject.client.NewsClient;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.mapper.CommentMapper;
import by.clevertec.commentsproject.repository.CommentRepository;
import by.clevertec.commentsproject.service.CommentService;
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

@Service
@RequiredArgsConstructor
@EnableCaching
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsClient newsClient;


    @Transactional
    @Override
    @CachePut(value = "comment", key = "#result.id")
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


    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "comment")
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() ->
                        EntityNotFoundExceptionCustom.of(Comment.class, id));
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    @CachePut(value = "comment", key = "#id")
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(Comment.class, id));

        commentMapper.updateFromDto(commentRequestDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @CacheEvict(value = "comment", key = "#id")
    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(Comment.class, id));
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> getAllComment(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(commentMapper::toDto);
    }

    @Transactional
    @Override
    public void deleteCommentsByNewsId(Long newsId) {
        commentRepository.deleteByNewsId(newsId);
    }


    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable) {
        return commentRepository.findByNewsId(newsId, pageable)
                .map(commentMapper::toDto);

    }

}

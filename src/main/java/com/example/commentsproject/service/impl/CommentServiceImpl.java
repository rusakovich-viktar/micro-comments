package com.example.commentsproject.service.impl;


import by.clevertec.exception.EntityNotFoundExceptionCustom;
import com.example.commentsproject.dto.request.CommentRequestDto;
import com.example.commentsproject.dto.response.CommentResponseDto;
import com.example.commentsproject.dto.response.NewsResponseDto;
import com.example.commentsproject.entity.Comment;
import com.example.commentsproject.mapper.CommentMapper;
import com.example.commentsproject.repository.CommentRepository;
import com.example.commentsproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@EnableCaching
public class CommentServiceImpl implements CommentService {

    private final String PORT_OTHER_MICROSERVICE = "8081";
    private final String PREFIX_HTTP = "http://";
    @Value("${map.value}")
    private String IP;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final WebClient webClient;

    @Transactional
    @Override
    @CachePut(value = "comment", key = "#result.id")
    public CommentResponseDto createComment(Long newsId, CommentRequestDto commentRequestDto) {

        findNewsById(newsId);

        Comment comment = commentMapper.toEntity(commentRequestDto);
        comment.setNewsId(newsId);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "comment")
    public CommentResponseDto getCommentById(Long newsId, Long id) {

        findNewsById(newsId);

        Comment comment = getCommentById(id);

        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    @CachePut(value = "comment", key = "#id")
    public CommentResponseDto updateComment(Long newsId, Long id, CommentRequestDto commentRequestDto) {

        findNewsById(newsId);

        Comment comment = getCommentById(id);

        commentMapper.updateFromDto(commentRequestDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @Transactional
    @CacheEvict(value = "news", key = "#id")
    @Override
    public void deleteComment(Long newsId, Long id) {

        findNewsById(newsId);

        Comment comment = getCommentById(id);

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> getAllCommentsByNewsId(Long newsId, Pageable pageable) {

        findNewsById(newsId);

        Page<Comment> comments = commentRepository.findByNewsId(newsId, pageable);

        return comments.map(commentMapper::toDto);
    }

    private void findNewsById(Long newsId) {
        String newsServiceUrl = PREFIX_HTTP + IP + ":" + PORT_OTHER_MICROSERVICE + "/news";
        String url = newsServiceUrl + "/" + newsId;
        NewsResponseDto news = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(NewsResponseDto.class)
                .block();

        if (news == null) {
            throw EntityNotFoundExceptionCustom.of(NewsResponseDto.class, newsId);
        }
    }

    private Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(Comment.class, id));
    }

    public Page<Comment> search(String queryString, Pageable pageable) {
        return commentRepository.search(queryString, pageable);
    }

}

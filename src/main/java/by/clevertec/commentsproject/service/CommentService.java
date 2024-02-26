package by.clevertec.commentsproject.service;


import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto);

    CommentResponseDto getCommentById(Long id);

    CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto);

    void deleteComment(Long id);

    Page<CommentResponseDto> getAllComment(Pageable pageable);

    void deleteCommentsByNewsId(Long newsId);

    Page<CommentResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable);


}

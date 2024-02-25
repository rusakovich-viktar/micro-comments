package com.example.commentsproject.service;


import com.example.commentsproject.dto.request.CommentRequestDto;
import com.example.commentsproject.dto.response.CommentResponseDto;
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

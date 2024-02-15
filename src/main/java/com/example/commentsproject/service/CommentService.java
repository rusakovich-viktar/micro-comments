package com.example.commentsproject.service;


import com.example.commentsproject.dto.request.CommentRequestDto;
import com.example.commentsproject.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(Long newsId, CommentRequestDto commentRequestDto);

    CommentResponseDto getCommentById(Long newsId, Long id);

    CommentResponseDto updateComment(Long newsId, Long id, CommentRequestDto commentRequestDto);//

    void deleteComment(Long newsId, Long id);

    Page<CommentResponseDto> getAllCommentsByNewsId(Long newsId, Pageable pageable);
}

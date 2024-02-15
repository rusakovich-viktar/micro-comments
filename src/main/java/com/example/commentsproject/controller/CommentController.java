package com.example.commentsproject.controller;

import com.example.commentsproject.dto.request.CommentRequestDto;
import com.example.commentsproject.dto.response.CommentResponseDto;
import com.example.commentsproject.entity.Comment;
import com.example.commentsproject.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news/{newsId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long newsId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(
                commentService.createComment(newsId, commentRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long newsId, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(newsId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long newsId, @PathVariable Long id, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(newsId, id, commentRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long newsId, @PathVariable Long id) {
        commentService.deleteComment(newsId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllCommentsByNewsId(newsId, pageable));
    }


}

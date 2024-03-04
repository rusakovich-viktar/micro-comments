package by.clevertec.commentsproject.controller;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.service.CommentService;
import by.clevertec.commentsproject.util.Constant.BaseApi;
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

@RestController
@RequestMapping(BaseApi.COMMENTS)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(BaseApi.NEWS_NEWS_ID)
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long newsId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(
                commentService.createComment(newsId, commentRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping(BaseApi.ID)
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PutMapping(BaseApi.ID)
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(id, commentRequestDto));
    }

    @DeleteMapping(BaseApi.ID)
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getAllComment(Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllComment(pageable));
    }

    @DeleteMapping(BaseApi.NEWS_NEWS_ID)
    public ResponseEntity<Void> deleteCommentsByNewsId(@PathVariable Long newsId) {
        commentService.deleteCommentsByNewsId(newsId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(BaseApi.NEWS_NEWS_ID)
    ResponseEntity<Page<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

}

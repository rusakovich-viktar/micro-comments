package by.clevertec.commentsproject.controller;


import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
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

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/news/{newsId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long newsId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return new ResponseEntity<>(
                commentService.createComment(newsId, commentRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(id, commentRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getAllComment(Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllComment(pageable));
    }

    @DeleteMapping("/news/{newsId}")
    public ResponseEntity<Void> deleteCommentsByNewsId(@PathVariable Long newsId) {
        commentService.deleteCommentsByNewsId(newsId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/news/{newsId}")
    ResponseEntity<Page<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

}

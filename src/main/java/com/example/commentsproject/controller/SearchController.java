package com.example.commentsproject.controller;

import com.example.commentsproject.entity.Comment;
import com.example.commentsproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<Comment>> search(@RequestParam String query, Pageable pageable) {
        Page<Comment> results = commentService.search(query, pageable);
        return ResponseEntity.ok(results);
    }
}

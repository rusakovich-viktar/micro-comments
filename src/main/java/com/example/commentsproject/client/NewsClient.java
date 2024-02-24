package com.example.commentsproject.client;


import com.example.commentsproject.dto.response.NewsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-news", url = "http://localhost:8081")
public interface NewsClient {

    @GetMapping("/api/news/{id}")
    ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id);


}
package by.clevertec.commentsproject.controller;

import static by.clevertec.commentsproject.util.TestConstant.ExceptionMessages.EXCEPTION_OCCURRED_DURING_TEST;
import static by.clevertec.commentsproject.util.TestConstant.Path.COMMENTS_NEWS_URL;
import static by.clevertec.commentsproject.util.TestConstant.Path.COMMENTS_URL;
import static by.clevertec.commentsproject.util.TestConstant.Path.HTTP_LOCALHOST;
import static by.clevertec.commentsproject.util.TestConstant.UPDATED_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.util.DataTestBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerIntegrationTest {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        int numThreads = 6;
        ExecutorService service = Executors.newFixedThreadPool(numThreads);

        try {
            for (int i = 0; i < numThreads; i++) {
                service.submit(() -> {
                    try {
                        News news = DataTestBuilder.builder()
                                .build()
                                .buildNews();

                        Comment newComment = DataTestBuilder.builder()
                                .build()
                                .buildComment(news);

                        ResponseEntity<Void> postResponse = restTemplate
                                .postForEntity(HTTP_LOCALHOST + port + COMMENTS_NEWS_URL + newComment.getNews(), newComment, Void.class);
                        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

                        Long id = newComment.getId();

                        ResponseEntity<CommentResponseDto> getResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + COMMENTS_URL + id,
                                        CommentResponseDto.class, id);
                        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
                        assertEquals(newComment.getText(), getResponse.getBody().getText());

                        CommentRequestDto updatedComment = DataTestBuilder.builder()
                                .withText(UPDATED_TEXT)
                                .build()
                                .buildCommentRequestDto();

                        restTemplate.put(HTTP_LOCALHOST + port + COMMENTS_URL + id, updatedComment, id);

                        ResponseEntity<CommentResponseDto> updatedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + COMMENTS_URL + id,
                                        CommentResponseDto.class, id);
                        assertEquals(HttpStatus.OK, updatedGetResponse.getStatusCode());
                        assertEquals(updatedComment.getText(), updatedGetResponse.getBody().getText());

                        restTemplate.delete(HTTP_LOCALHOST + port + COMMENTS_URL + id, id);

                        ResponseEntity<CommentResponseDto> deletedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + COMMENTS_URL + id,
                                        CommentResponseDto.class, id);
                        assertEquals(HttpStatus.NOT_FOUND, deletedGetResponse.getStatusCode());
                    } catch (Exception e) {
                        fail(EXCEPTION_OCCURRED_DURING_TEST + e.getMessage());
                    }
                });
            }
        } finally {
            service.shutdown();
            service.awaitTermination(60, TimeUnit.SECONDS);
        }
    }
}


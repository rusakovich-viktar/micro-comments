package by.clevertec.commentsproject.cache.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.commentsproject.cache.Cache;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.mapper.CommentMapper;
import by.clevertec.commentsproject.repository.CommentRepository;
import by.clevertec.commentsproject.service.CommentService;
import by.clevertec.commentsproject.util.DataTestBuilder;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@RequiredArgsConstructor
class CommentProxyTest {

    private final CommentProxy commentProxy;

    @MockBean
    private final CommentMapper commentMapper;

    @MockBean
    private final CommentRepository commentRepository;

    @MockBean
    private final CommentService commentService;


    @MockBean
    private final ProceedingJoinPoint proceedingJoinPoint;

    private final StampedLock lock = new StampedLock();


    @Nested
    class TestGetComment {

        @Test
        void testGetCommentReturnCommentFromCache_whenCommentInCache() throws Throwable {
            CommentResponseDto commentResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            when(proceedingJoinPoint.getArgs())
                    .thenReturn(new Object[]{commentResponseDto.getId()});
            when(proceedingJoinPoint.proceed())
                    .thenReturn(commentResponseDto);

            commentProxy.createComment(commentResponseDto);

            Object result = commentProxy.getComment(proceedingJoinPoint);

            assertTrue(result instanceof CommentResponseDto);
            CommentResponseDto resultDto = (CommentResponseDto) result;
            assertEquals(commentResponseDto.getId(), resultDto.getId());
            assertEquals(commentResponseDto.getTime(), resultDto.getTime());
            assertEquals(commentResponseDto.getUpdateTime(), resultDto.getUpdateTime());
            assertEquals(commentResponseDto.getText(), resultDto.getText());
            assertEquals(commentResponseDto.getUsername(), resultDto.getUsername());
            assertEquals(commentResponseDto.getNewsId(), resultDto.getNewsId());

            verify(proceedingJoinPoint, times(0)).proceed();
        }

        @Test
        void testGetCommentReturnCommentFromCache_whenOptimisticLockInvalidated() throws Throwable {
            CommentResponseDto commentResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            when(proceedingJoinPoint.getArgs())
                    .thenReturn(new Object[]{commentResponseDto.getId()});
            when(proceedingJoinPoint.proceed())
                    .thenReturn(commentResponseDto);

            commentProxy.createComment(commentResponseDto);

            lock.writeLock();

            Object result = commentProxy.getComment(proceedingJoinPoint);

            assertTrue(result instanceof CommentResponseDto);
            CommentResponseDto resultDto = (CommentResponseDto) result;
            assertEquals(commentResponseDto.getId(), resultDto.getId());
            assertEquals(commentResponseDto.getTime(), resultDto.getTime());
            assertEquals(commentResponseDto.getUpdateTime(), resultDto.getUpdateTime());
            assertEquals(commentResponseDto.getText(), resultDto.getText());
            assertEquals(commentResponseDto.getUsername(), resultDto.getUsername());
            assertEquals(commentResponseDto.getNewsId(), resultDto.getNewsId());

            verify(proceedingJoinPoint, times(0)).proceed();
        }

    }

    @Test
    void testCreateComment() throws Throwable {

        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        Comment comment = DataTestBuilder.builder()
                .build()
                .buildComment(news);

        CommentResponseDto expected = DataTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                .build()
                .buildCommentRequestDto();

        when(commentMapper.toEntity(commentRequestDto)).thenReturn(comment);
        when(commentService.getCommentById(comment.getId())).thenReturn(expected);

        commentProxy.createComment(expected);

        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{comment.getId()});
        when(proceedingJoinPoint.proceed()).thenReturn(expected);

        CommentResponseDto result = (CommentResponseDto) commentProxy.getComment(proceedingJoinPoint);

        assertEquals(expected.getId(), result.getId());

    }

    @Test
    void testDeleteComment() throws Exception {
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        Comment comment = DataTestBuilder.builder()
                .build()
                .buildComment(news);

        Field userCacheField = CommentProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<Long, Object>> userCacheRef =
                (AtomicReference<Cache<Long, Object>>) userCacheField.get(commentProxy);

        Cache<Long, Object> userCache = userCacheRef.get();
        userCache.put(comment.getId(), comment);

        commentProxy.deleteComment(comment.getId());

        assertNull(userCache.get(comment.getId()));
    }

    @Test
    void testUpdateComment() throws Exception {
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        Comment comment = DataTestBuilder.builder()
                .build()
                .buildComment(news);

        CommentResponseDto expected = DataTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Field userCacheField = CommentProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<Long, Object>> userCacheRef =
                (AtomicReference<Cache<Long, Object>>) userCacheField.get(commentProxy);

        Cache<Long, Object> userCache = userCacheRef.get();

        commentProxy.updateComment(comment.getId(), expected);

        assertEquals(expected, userCache.get(comment.getId()));
    }


}
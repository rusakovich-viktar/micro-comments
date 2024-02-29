package by.clevertec.commentsproject.cache.proxy;

import by.clevertec.commentsproject.cache.Cache;
import by.clevertec.commentsproject.cache.impl.LfuCache;
import by.clevertec.commentsproject.cache.impl.LruCache;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
@Profile("dev")
public class CommentProxy {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer maxCapacity;

    private final AtomicReference<Cache<Long, Object>> userCache = new AtomicReference<>(createCache());
    private final StampedLock lock = new StampedLock();


    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) " +
            "&& execution(* by.clevertec.commentsproject.service.CommentService.getCommentById(..))")
    public Object getComment(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];

        long stamp = lock.tryOptimisticRead();
        Object cachedObject = userCache.get().get(id);
        CommentResponseDto commentResponseDto = null;
        if (cachedObject instanceof CommentResponseDto) {
            commentResponseDto = (CommentResponseDto) cachedObject;
        }
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                commentResponseDto = (CommentResponseDto) userCache.get().get(id);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        if (commentResponseDto != null) {
            log.info("Comment with id {} was retrieved from cache", id);
            return commentResponseDto;
        }

        log.info("Comment with id {} was not found in cache, retrieving from database", id);
        Object result = joinPoint.proceed();
        if (result instanceof CommentResponseDto commentDto) {
            userCache.get().put(id, result);
            return commentDto;
        }
        return result;
    }


    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && " +
            "execution(* by.clevertec.commentsproject.service.CommentService.createComment(..))", returning = "response")
    public void createComment(CommentResponseDto response) {
        userCache.get().put(response.getId(), response);
        log.info("Comment with id {} was added to cache", response.getId());

    }


    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) " +
            "&& execution(* by.clevertec.commentsproject.service.CommentService.deleteComment(Long)) && args(id)",
            argNames = "id")
    public void deleteComment(Long id) {
        userCache.get().remove(id);
        log.info("Comment with id {} was removed from cache", id);

    }

    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) &&" +
            " execution(* by.clevertec.commentsproject.service.CommentService.updateComment(Long, ..)) && args(id, ..)",
            argNames = "id,retVal", returning = "retVal")
    public void updateComment(Long id, CommentResponseDto retVal) {
        userCache.get().put(id, retVal);
        log.info("Comment with id {} was updated in cache", id);


    }

    private Cache<Long, Object> createCache() {
        if (maxCapacity == null) {
            maxCapacity = 50;
        }
        if ("LFU".equals(algorithm)) {
            return new LfuCache<>(maxCapacity);
        } else {
            return new LruCache<>(maxCapacity);
        }
    }
}

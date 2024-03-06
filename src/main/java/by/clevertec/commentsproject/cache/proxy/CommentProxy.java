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

/**
 * Прокси-класс для управления кэшем комментариев.
 */
@Slf4j
@Aspect
@Component
@Profile("dev")
@RequiredArgsConstructor
public class CommentProxy {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer maxCapacity;

    private final AtomicReference<Cache<Long, Object>> userCache = new AtomicReference<>(createCache());
    private final StampedLock lock = new StampedLock();

    /**
     * Получает комментарии из кэша или из базы данных, если в кэше их нет.
     *
     * @param joinPoint точка присоединения
     * @return объект комментариев
     * @throws Throwable в случае ошибки
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) "
            + "&& execution(* by.clevertec.commentsproject.service.CommentService.getCommentById(..))")
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

    /**
     * Добавляет новые комментарии в кэш после их создания.
     *
     * @param response объект комментариев
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && "
            + "execution(* by.clevertec.commentsproject.service.CommentService.createComment(..))",
            returning = "response")
    public void createComment(CommentResponseDto response) {
        userCache.get().put(response.getId(), response);
        log.info("Comment with id {} was added to cache", response.getId());

    }

    /**
     * Удаляет комментарии из кэша после их удаления.
     *
     * @param id идентификатор комментариев
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) "
            + "&& execution(* by.clevertec.commentsproject.service.CommentService.deleteComment(Long)) && args(id)",
            argNames = "id")
    public void deleteComment(Long id) {
        userCache.get().remove(id);
        log.info("Comment with id {} was removed from cache", id);

    }

    /**
     * Обновляет комментарии в кэше после их обновления.
     *
     * @param id     идентификатор комментариев
     * @param retVal объект комментариев
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) &&"
            + " execution(* by.clevertec.commentsproject.service.CommentService.updateComment(Long, ..)) && args(id, ..)",
            argNames = "id,retVal", returning = "retVal")
    public void updateComment(Long id, CommentResponseDto retVal) {
        userCache.get().put(id, retVal);
        log.info("Comment with id {} was updated in cache", id);

    }

    /**
     * Создает кэш.
     *
     * @return объект кэша
     */
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



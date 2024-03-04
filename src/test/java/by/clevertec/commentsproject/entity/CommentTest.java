package by.clevertec.commentsproject.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentTest {

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment();
    }

    @Test
    void testPrePersist() {
        comment.prePersist();

        LocalDateTime time = comment.getTime();
        LocalDateTime updateTime = comment.getUpdateTime();

        assertNotNull(time);
        assertNotNull(updateTime);
        assertEquals(time, updateTime);
    }

    @Test
    void testPreUpdate() throws Exception {
        comment.prePersist();
        LocalDateTime initialTime = comment.getTime();

        Thread.sleep(1000);

        comment.preUpdate();

        LocalDateTime updateTime = comment.getUpdateTime();

        assertNotNull(updateTime);
        assertNotEquals(initialTime, updateTime);
    }
}

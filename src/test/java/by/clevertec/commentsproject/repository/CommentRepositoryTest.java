package by.clevertec.commentsproject.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.util.TestConstant;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:db/insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;

    @Test
    void testFindByNewsIdShouldReturnComments_whenTheyExist() {
        // given
        Long expectedNewsId = TestConstant.ID_ONE;

        // when
        Page<Comment> actual = commentRepository.findByNewsId(expectedNewsId, PageRequest.of(0, 10));

        // then
        assertFalse(actual.isEmpty());
        actual.forEach(comment -> assertEquals(expectedNewsId, comment.getNews().getId()));
    }

    @Test
    void testDeleteByNewsId() {
        // given
        Long newsId = TestConstant.ID_ONE;

        // when
        commentRepository.deleteByNewsId(newsId);

        // then
        Page<Comment> commentsAfterDelete = commentRepository.findByNewsId(newsId, PageRequest.of(0, 10));
        assertTrue(commentsAfterDelete.isEmpty());
    }

}

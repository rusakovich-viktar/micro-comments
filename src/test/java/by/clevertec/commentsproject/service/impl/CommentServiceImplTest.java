package by.clevertec.commentsproject.service.impl;

import static by.clevertec.commentsproject.util.TestConstant.ExceptionMessages.POSTFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.commentsproject.util.TestConstant.ExceptionMessages.PREFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.commentsproject.util.TestConstant.ID_ONE;
import static by.clevertec.commentsproject.util.TestConstant.NEW_TEXT;
import static by.clevertec.commentsproject.util.TestConstant.NEW_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.commentsproject.client.NewsClient;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.mapper.CommentMapper;
import by.clevertec.commentsproject.repository.CommentRepository;
import by.clevertec.commentsproject.util.DataTestBuilder;
import by.clevertec.exception.EntityNotFoundExceptionCustom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsClient newsClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private final Long newsId = ID_ONE;

    @BeforeEach
    void setUp() {

        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        comment = DataTestBuilder.builder()
                .build()
                .buildComment(news);

    }

    @Nested
    class CreateCommentTests {

        @Test
        void createCommentShouldReturnCommentResponseDto() {

            CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            CommentResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            when(newsClient.getNewsById(newsId)).thenReturn(ResponseEntity.ok(newsResponseDto));
            when(commentMapper.toEntity(commentRequestDto)).thenReturn(comment);
            when(commentRepository.save(comment)).thenReturn(comment);
            when(commentMapper.toDto(comment)).thenReturn(expected);

            CommentResponseDto result = commentService.createComment(newsId, commentRequestDto);

            // then
            assertEquals(expected, result);
        }

        @Test
        void createCommentShouldThrowException_whenNewsNotFound() {

            // given
            Long invalidNewsId = -1L;
            CommentRequestDto requestDto = new CommentRequestDto();

            // when
            when(newsClient.getNewsById(invalidNewsId)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

            // then
            assertThrows(EntityNotFoundExceptionCustom.class, () -> commentService.createComment(invalidNewsId, requestDto));
        }

        @Test
        void createCommentShouldThrowException_whenTextIsEmpty() {

            CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            commentRequestDto.setText("");

            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsClient.getNewsById(newsId)).thenReturn(ResponseEntity.ok(newsResponseDto));

            assertThrows(NullPointerException.class, () -> commentService.createComment(newsId, commentRequestDto));
        }

    }

    @Nested
    class GetNewsTests {

        @Test
        void testGetCommentByIdShouldReturnCommentResponseDto_whenCommentIsExist() {

            // given
            CommentResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            when(commentRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(comment));
            when(commentMapper.toDto(comment))
                    .thenReturn(expected);

            // when
            CommentResponseDto actual = commentService.getCommentById(ID_ONE);

            // then
            assertEquals(expected, actual);
            verify(commentRepository)
                    .findById(ID_ONE);
            verify(commentMapper)
                    .toDto(comment);
        }

        @Test
        void testGetCommentByIdShouldThrowNotFound_whenCommentDoesNotExist() {
            // given
            when(commentRepository.findById(ID_ONE)).thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    commentService.getCommentById(ID_ONE));

            // then
            assertEquals(Comment.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class UpdateCommentTests {

        @Test
        void testUpdateCommentShouldReturnCommentResponseDto_whenCorrectCommentRequestDto() {
            // given
            CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                    .withTitle(NEW_TITLE)
                    .withText(NEW_TEXT)
                    .build()
                    .buildCommentRequestDto();

            when(commentRepository.findById(ID_ONE)).thenReturn(Optional.of(comment));
            doAnswer(invocation -> {
                Comment target = invocation.getArgument(1);
                target.setText(commentRequestDto.getText());
                target.setUsername(commentRequestDto.getUsername());
                return null;
            }).when(commentMapper).updateFromDto(commentRequestDto, comment);

            // when
            commentService.updateComment(ID_ONE, commentRequestDto);

            // then
            verify(commentRepository)
                    .save(comment);
            verify(commentMapper)
                    .updateFromDto(commentRequestDto, comment);
            assertEquals(commentRequestDto.getNewsId(), comment.getNews().getId());
            assertEquals(commentRequestDto.getUsername(), comment.getUsername());
            assertEquals(commentRequestDto.getText(), comment.getText());

        }

        @Test
        void testUpdateCommentShouldThrowNotFound_whenCommentDoesNotExist() {
            // given
            CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            when(commentRepository.findById(ID_ONE))
                    .thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    commentService.updateComment(ID_ONE, commentRequestDto));

            // then
            assertEquals(Comment.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class DeleteCommentTests {

        @Test
        void testDeleteCommentShouldDeleteComment_whenCommentExist() {
            // given

            when(commentRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(comment));

            // when
            commentService
                    .deleteComment(ID_ONE);

            // then

            verify(commentRepository)
                    .delete(comment);
        }

        @Test
        void testDeleteCommentShouldThrowNotFound_whenCommentDoesNotExist() {
            // given
            when(commentRepository.findById(ID_ONE)).thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    commentService.deleteComment(ID_ONE));

            // then
            assertEquals(Comment.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class GetAllCommentTests {

        @Test
        void testGetAllCommentShouldReturnListOfComment() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Page<Comment> page = new PageImpl<>(List.of(comment));

            CommentResponseDto commentResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            when(commentRepository.findAll(pageable))
                    .thenReturn(page);
            when(commentMapper.toDto(comment))
                    .thenReturn(commentResponseDto);

            // when
            Page<CommentResponseDto> actual = commentService.getAllComment(pageable);

            // then
            assertEquals(1, actual.getContent().size());
            assertEquals(comment.getId(), actual.getContent().get(0).getId());
            assertEquals(comment.getTime(), actual.getContent().get(0).getTime());
            assertEquals(comment.getUpdateTime(), actual.getContent().get(0).getUpdateTime());
            assertEquals(comment.getText(), actual.getContent().get(0).getText());
            assertEquals(comment.getUsername(), actual.getContent().get(0).getUsername());
            verify(commentRepository).findAll(pageable);
            verify(commentMapper).toDto(comment);
        }

        @Test
        void testGetAllCommentShouldEmptyList_whenNoCommentExist() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Page<Comment> page = new PageImpl<>(List.of());

            when(commentRepository.findAll(pageable))
                    .thenReturn(page);

            // when
            Page<CommentResponseDto> actual = commentService.getAllComment(pageable);

            // then
            assertTrue(actual.getContent().isEmpty());
        }

    }

    @Test
    void deleteCommentsByNewsIdShouldCallRepository() {

        // given & when
        commentService.deleteCommentsByNewsId(newsId);

        // then
        verify(commentRepository, times(1)).deleteByNewsId(newsId);
    }

    @Test
    void getCommentsByNewsIdShouldReturnPageOfComments() {

        Pageable pageable = PageRequest.of(0, 5);

        CommentResponseDto commentResponseDto = DataTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));

        when(commentRepository.findByNewsId(commentResponseDto.getNewsId(), pageable)).thenReturn(commentPage);
        when(commentMapper.toDto(comment)).thenReturn(commentResponseDto);

        // when
        Page<CommentResponseDto> result = commentService.getCommentsByNewsId(commentResponseDto.getNewsId(), pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(commentResponseDto, result.getContent().get(0));
    }
}

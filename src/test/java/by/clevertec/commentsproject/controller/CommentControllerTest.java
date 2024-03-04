package by.clevertec.commentsproject.controller;

import static by.clevertec.commentsproject.util.TestConstant.Attributes.FIVE;
import static by.clevertec.commentsproject.util.TestConstant.Attributes.ONE_ZERO_ZERO;
import static by.clevertec.commentsproject.util.TestConstant.Attributes.PAGE;
import static by.clevertec.commentsproject.util.TestConstant.Attributes.SIZE;
import static by.clevertec.commentsproject.util.TestConstant.COMMENT;
import static by.clevertec.commentsproject.util.TestConstant.ExceptionMessages.POSTFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.commentsproject.util.TestConstant.ExceptionMessages.PREFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.commentsproject.util.TestConstant.ID_ONE;
import static by.clevertec.commentsproject.util.TestConstant.INVALID_ID;
import static by.clevertec.commentsproject.util.TestConstant.Path.COMMENTS;
import static by.clevertec.commentsproject.util.TestConstant.Path.COMMENTS_NEWS_URL;
import static by.clevertec.commentsproject.util.TestConstant.Path.COMMENTS_URL;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.commentsproject.client.NewsClient;
import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.service.CommentService;
import by.clevertec.commentsproject.util.DataTestBuilder;
import by.clevertec.commentsproject.util.TestConstant;
import by.clevertec.exception.EntityNotFoundExceptionCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final CommentService commentService;

    @MockBean
    private final NewsClient newsClient;

    @Nested
    class TestGetById {

        @Test
        void getByIdShouldReturnCommentResponseDto() throws Exception {
            CommentResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            // when
            when(commentService.getCommentById(expected.getId()))
                    .thenReturn(expected);

            // then
            mockMvc.perform(get(COMMENTS_URL + expected.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        }

        @Test
        void getByIdShouldThrowNotFound_whenInvalidId() throws Exception {

            Long invalidId = INVALID_ID;
            String url = COMMENTS_URL + invalidId;
            EntityNotFoundExceptionCustom exception = new EntityNotFoundExceptionCustom
                    (COMMENT + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + invalidId + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE);

            // when
            when(commentService.getCommentById(invalidId))
                    .thenThrow(exception);

            // then
            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(exception.getMessage(),
                            result.getResolvedException().getMessage()));
        }
    }

    @Nested
    class TestCreateComment {

        @Test
        void createCommentShouldReturnCommentResponseDto() throws Exception {

            // given
            CommentRequestDto requestDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            CommentResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            ResponseEntity<NewsResponseDto> responseEntity = ResponseEntity.ok(newsResponseDto);

            // when
            when(newsClient.getNewsById(anyLong()))
                    .thenReturn(responseEntity);
            when(commentService.createComment(anyLong(), any(CommentRequestDto.class)))
                    .thenReturn(expected);

            mockMvc.perform(post(COMMENTS_NEWS_URL + expected.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }

        @Test
        void createCommentShouldReturnBadRequest_whenInvalidRequest() throws Exception {

            // given
            CommentRequestDto invalidCommentRequestDto = new CommentRequestDto();

            // then
            mockMvc.perform(post(COMMENTS_NEWS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidCommentRequestDto)))
                    .andExpect(status().isNotFound());

        }

    }

    @Nested
    class TestUpdateComment {

        @Test
        void updateCommentShouldReturnCommentResponseDto() throws Exception {

            // given
            CommentRequestDto commentRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            CommentResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            // when
            when(commentService.updateComment(anyLong(), any(CommentRequestDto.class)))
                    .thenReturn(expected);

            // then
            mockMvc.perform(put(COMMENTS_URL + 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        }

        @Test
        void updateCommentShouldReturnBadRequest_whenInvalidRequest() throws Exception {

            // given
            CommentRequestDto invalidCommentRequestDto = new CommentRequestDto();

            // when
            mockMvc.perform(put(COMMENTS_URL + ID_ONE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidCommentRequestDto)))
                    .andExpect(status().isBadRequest());

            // then
            verify(commentService, times(0))
                    .updateComment(anyLong(), any());
        }
    }

    @Nested
    class TestDeleteComment {

        @Test
        void deleteCommentShouldReturnNoContent() throws Exception {

            // when
            mockMvc.perform(delete(COMMENTS_URL + ID_ONE))
                    .andExpect(status().isNoContent());

            // then
            verify(commentService, times(1))
                    .deleteComment(ID_ONE);
        }

    }

    @Nested
    class TestGetAllNews {

        @Test
        void getAllCommentShouldReturnPageOfComments() throws Exception {

            // given
            int pageNumber = 1;
            int pageSize = 15;
            CommentResponseDto commentsOne = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            CommentResponseDto commentsTwo = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            List<CommentResponseDto> expectedComment = List.of(commentsOne, commentsTwo);
            Page<CommentResponseDto> page = new PageImpl<>(expectedComment);

            // when
            when(commentService.getAllComment(any(Pageable.class)))
                    .thenReturn(page);

            // then
            mockMvc.perform(get(COMMENTS)
                            .param(PAGE, String.valueOf(pageNumber))
                            .param(SIZE, String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(expectedComment))))
                    .andExpect(jsonPath("$.content", hasSize(expectedComment.size())))
                    .andExpect(jsonPath("$.content[0].id").value(commentsOne.getId().intValue()))
                    .andExpect(jsonPath("$.content[0].text").value(commentsOne.getText()))
                    .andExpect(jsonPath("$.content[0].username").value(commentsOne.getUsername()))
                    .andExpect(jsonPath("$.content[0].newsId").value(commentsOne.getNewsId().intValue()))
                    .andExpect(jsonPath("$.content[1].id").value(commentsTwo.getId().intValue()))
                    .andExpect(jsonPath("$.content[1].text").value(commentsTwo.getText()))
                    .andExpect(jsonPath("$.content[1].username").value(commentsTwo.getUsername()))
                    .andExpect(jsonPath("$.content[1].newsId").value(commentsTwo.getNewsId().intValue()));
        }

        @Test
        void getAllCommentShouldReturnEmptyPage_whenNoComments() throws Exception {
            Page<CommentResponseDto> page = Page.empty();

            when(commentService.getAllComment(any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get(COMMENTS)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        void getAllCommentShouldReturnEmptyPage_whenPageOutOfRange() throws Exception {
            Page<CommentResponseDto> page = Page.empty();

            when(commentService.getAllComment(any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get(COMMENTS)
                            .param(PAGE, ONE_ZERO_ZERO)
                            .param(SIZE, FIVE)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

    }

    @Nested
    class TestGetCommentsByNewsId {

        @Test
        void getCommentsByNewsIdShouldReturnPageOfComments() throws Exception {

            // given
            CommentResponseDto responseDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            List<CommentResponseDto> expectedComment = Collections.singletonList(responseDto);
            Page<CommentResponseDto> page = new PageImpl<>(expectedComment);

            // when
            when(commentService.getCommentsByNewsId(responseDto.getNewsId(),
                    PageRequest.of(0, 5)))
                    .thenReturn(page);

            // then
            mockMvc.perform(get(COMMENTS_NEWS_URL + responseDto.getNewsId())
                            .param(PAGE, TestConstant.Attributes.ZERO)
                            .param(SIZE, FIVE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].id").value(responseDto.getId().intValue()))
                    .andExpect(jsonPath("$.content[0].text").value(responseDto.getText()))
                    .andExpect(jsonPath("$.content[0].username").value(responseDto.getUsername()))
                    .andExpect(jsonPath("$.content[0].newsId").value(responseDto.getNewsId().intValue()));
        }

        @Test
        void getCommentsByNewsIdShouldReturnEmptyPage_whenNoComments() throws Exception {

            // given
            Long newsId = 1L;
            Page<CommentResponseDto> page = Page.empty();

            when(commentService.getCommentsByNewsId(anyLong(), any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get(COMMENTS_NEWS_URL + newsId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        void getCommentsByNewsIdShouldReturnEmptyPage_whenPageOutOfRange() throws Exception {

            // given
            Long newsId = 1L;
            Page<CommentResponseDto> page = Page.empty();

            when(commentService.getCommentsByNewsId(anyLong(), any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get(COMMENTS_NEWS_URL + newsId)
                            .param(PAGE, ONE_ZERO_ZERO)
                            .param(SIZE, FIVE)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        void getCommentsByNewsIdShouldReturnNotFound_whenInvalidNewsId() throws Exception {

            // given
            Long invalidNewsId = -1L;
            when(commentService.getCommentsByNewsId(anyLong(), any(Pageable.class)))
                    .thenThrow(EntityNotFoundExceptionCustom.of(NewsResponseDto.class, invalidNewsId));

            mockMvc.perform(get(COMMENTS_NEWS_URL + invalidNewsId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    class DeleteCommentsByNewsId {

        @Test
        void deleteCommentsByNewsIdShouldReturnNoContentWhenCommentsExist() throws Exception {

            // given
            Long newsId = 1L;

            doNothing()
                    .when(commentService).deleteCommentsByNewsId(newsId);

            mockMvc.perform(delete(COMMENTS_NEWS_URL + newsId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteCommentsByNewsIdShouldReturnNoContentWhenInvalidNewsId() throws Exception {

            // given
            Long invalidNewsId = -1L;

            doNothing()
                    .when(commentService).deleteCommentsByNewsId(invalidNewsId);

            mockMvc.perform(delete(COMMENTS_NEWS_URL + invalidNewsId))
                    .andExpect(status().isNoContent());
        }

    }
}

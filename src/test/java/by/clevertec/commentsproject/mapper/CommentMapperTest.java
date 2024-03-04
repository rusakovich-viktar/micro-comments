package by.clevertec.commentsproject.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.Comment.Fields;
import by.clevertec.commentsproject.entity.News;
import by.clevertec.commentsproject.util.DataTestBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CommentMapperTest {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void testToDtoShouldReturnResponseDto() {
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        Comment comment = DataTestBuilder.builder()
                .build().buildComment(news);

        CommentResponseDto expected = DataTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        CommentResponseDto actual = commentMapper.toDto(comment);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Fields.updateTime, expected.getUpdateTime())
                .hasFieldOrPropertyWithValue(Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(Fields.text, expected.getText());
    }

    @Test
    void testToEntityShouldReturnComment() {
        CommentRequestDto dto = DataTestBuilder.builder()
                .build()
                .buildCommentRequestDto();
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();
        Comment expected = DataTestBuilder.builder()
                .build()
                .buildComment(news);

        Comment actual = commentMapper.toEntity(dto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Fields.news, expected.getNews());
    }

    @Test
    void testUpdateFromDtoShouldUpdateComment() {
        CommentRequestDto dto = DataTestBuilder.builder()
                .build()
                .buildCommentRequestDto();
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();
        Comment comment = DataTestBuilder.builder()
                .build()
                .buildComment(news);

        commentMapper.updateFromDto(dto, comment);

        assertThat(comment)
                .hasFieldOrPropertyWithValue(Fields.username, dto.getUsername())
                .hasFieldOrPropertyWithValue(Fields.text, dto.getText());
    }
}

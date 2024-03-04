package by.clevertec.commentsproject.util;

import static by.clevertec.commentsproject.util.TestConstant.LOCAL_DATE_TIME;
import static by.clevertec.commentsproject.util.TestConstant.NEWS_TEXT;
import static by.clevertec.commentsproject.util.TestConstant.NEWS_TITLE;
import static by.clevertec.commentsproject.util.TestConstant.USERNAME;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.request.NewsRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import by.clevertec.commentsproject.entity.News;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class DataTestBuilder {

    @Builder.Default
    private Long id = TestConstant.ID_ONE;

    @Builder.Default
    private LocalDateTime time = LOCAL_DATE_TIME;

    @Builder.Default
    private LocalDateTime updateTime = LOCAL_DATE_TIME;

    @Builder.Default
    private String title = NEWS_TITLE;

    @Builder.Default
    private String text = NEWS_TEXT;

    @Builder.Default
    private String username = USERNAME;

    @Builder.Default
    private List<CommentResponseDto> comments = new ArrayList<>();
    private List<Comment> commentsList;

    public News buildNews() {
        News news = new News();

        news.setId(id);
        news.setComments(commentsList);

        return news;
    }

    public Comment buildComment(News news) {
        Comment comment = new Comment();

        comment.setId(id);
        comment.setTime(time);
        comment.setUpdateTime(updateTime);
        comment.setText(text);
        comment.setUsername(username);
        comment.setNews(news);

        return comment;
    }

    public CommentResponseDto buildCommentResponseDto() {
        CommentResponseDto buildCommentResponseDto = new CommentResponseDto();

        buildCommentResponseDto.setId(id);
        buildCommentResponseDto.setTime(time);
        buildCommentResponseDto.setUpdateTime(time);
        buildCommentResponseDto.setText(text);
        buildCommentResponseDto.setUsername(username);
        buildCommentResponseDto.setNewsId(id);

        return buildCommentResponseDto;
    }

    public CommentRequestDto buildCommentRequestDto() {
        CommentRequestDto buildCommentRequestDto = new CommentRequestDto();

        buildCommentRequestDto.setText(text);
        buildCommentRequestDto.setUsername(username);
        buildCommentRequestDto.setNewsId(id);

        return buildCommentRequestDto;
    }

    public NewsResponseDto buildNewsResponseDto() {
        NewsResponseDto buildNewsResponseDto = new NewsResponseDto();

        buildNewsResponseDto.setId(id);
        buildNewsResponseDto.setTime(time);
        buildNewsResponseDto.setUpdateTime(time);
        buildNewsResponseDto.setTitle(title);
        buildNewsResponseDto.setText(text);

        return buildNewsResponseDto;
    }

    public NewsRequestDto buildNewsRequestDto() {
        NewsRequestDto buildNewsRequestDto = new NewsRequestDto();

        buildNewsRequestDto.setTitle(title);
        buildNewsRequestDto.setText(text);

        return buildNewsRequestDto;
    }

}

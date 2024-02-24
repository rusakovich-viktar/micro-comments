package com.example.commentsproject.mapper;


import com.example.commentsproject.dto.request.CommentRequestDto;
import com.example.commentsproject.dto.response.CommentResponseDto;
import com.example.commentsproject.entity.Comment;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @Mapping(source = "news.id", target = "newsId")
    CommentResponseDto toDto(Comment entity);

    @Mapping(source = "newsId", target = "news.id")
    Comment toEntity(CommentRequestDto dto);

    List<CommentResponseDto> toDtoList(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "news", ignore = true)
    void updateFromDto(CommentRequestDto dto, @MappingTarget Comment entity);

}
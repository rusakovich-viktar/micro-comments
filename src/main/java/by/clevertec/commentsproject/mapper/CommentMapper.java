package by.clevertec.commentsproject.mapper;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import by.clevertec.commentsproject.dto.request.CommentRequestDto;
import by.clevertec.commentsproject.dto.response.CommentResponseDto;
import by.clevertec.commentsproject.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования сущности "Комментарий" в DTO и обратно.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    /**
     * Преобразует сущность "Комментарий" в DTO.
     *
     * @param entity сущность "Комментарий"
     * @return DTO комментария
     */
    @Mapping(source = "news.id", target = "newsId")
    CommentResponseDto toDto(Comment entity);

    /**
     * Преобразует DTO комментария в сущность "Комментарий".
     *
     * @param dto DTO комментария
     * @return сущность "Комментарий"
     */
    @Mapping(source = "newsId", target = "news.id")
    Comment toEntity(CommentRequestDto dto);

    /**
     * Обновляет сущность "Комментарий" на основе DTO комментария.
     *
     * @param dto    DTO комментария
     * @param entity сущность "Комментарий"
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateFromDto(CommentRequestDto dto, @MappingTarget Comment entity);

}

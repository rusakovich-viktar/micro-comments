package by.clevertec.commentsproject.dto.request;

import static by.clevertec.commentsproject.util.Constant.Messages.TEXT_SHOULD_BE_MINIMUM_5_SYMBOLS;
import static by.clevertec.commentsproject.util.Constant.Messages.TITLE_CANNOT_BE_BLANK;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс для передачи данных о комментарии.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto implements Serializable {

    @NotBlank(message = TITLE_CANNOT_BE_BLANK)
    @Size(min = 5, message = TEXT_SHOULD_BE_MINIMUM_5_SYMBOLS)
    private String text;

    private String username;

    private Long newsId;

}

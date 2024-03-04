package by.clevertec.commentsproject.dto.request;

import static by.clevertec.commentsproject.util.Constant.Messages.TEXT_CANNOT_BE_BLANK;
import static by.clevertec.commentsproject.util.Constant.Messages.TITLE_CANNOT_BE_BLANK;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewsRequestDto implements Serializable {

    @NotBlank(message = TITLE_CANNOT_BE_BLANK)
    private String title;

    @NotBlank(message = TEXT_CANNOT_BE_BLANK)
    private String text;

}

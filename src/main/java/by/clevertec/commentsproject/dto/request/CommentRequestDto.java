package by.clevertec.commentsproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto implements Serializable {

    @NotBlank(message = "Text cannot be blank")
    @Size(min = 5, message = "Text should be minimum 5 symbols")
    private String text;

    private String username;

    private Long newsId;

}

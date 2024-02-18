package com.example.commentsproject.dto.request;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto implements Serializable {

    private String text;
    private String username;
    private Long newsId;

}

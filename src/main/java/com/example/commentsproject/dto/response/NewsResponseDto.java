package com.example.commentsproject.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsResponseDto implements Serializable {

    private Long id;
    private LocalDateTime time;
    private LocalDateTime updateTime;
    private String title;
    private String text;
}

package com.example.commentsproject.exception;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для представления ошибки.
 * Содержит информацию о времени ошибки, сообщении об ошибке и подробностях.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private String message;
    private String details;

    public ErrorResponse(String message, String details) {
        this.timestamp = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        this.message = message;
        this.details = details;
    }

}

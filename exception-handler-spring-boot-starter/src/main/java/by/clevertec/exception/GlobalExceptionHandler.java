package by.clevertec.exception;

import by.clevertec.util.Constant.ErrorMessages;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {
        String exceptionMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> "Error in field '" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorMessages.VALIDATION_ERROR, exceptionMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception) {
        return getErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundExceptionCustom.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundExceptionCustom exception) {
        return getErrorResponseEntity(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleException(NullPointerException exception) {
        return getErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected error occurred: ", exception);
        return getErrorResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseEntity(Exception exception, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), exception.getMessage()), httpStatus);
    }
}

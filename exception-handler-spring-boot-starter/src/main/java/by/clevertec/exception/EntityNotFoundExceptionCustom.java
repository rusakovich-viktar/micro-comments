package by.clevertec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundExceptionCustom extends RuntimeException {

    public EntityNotFoundExceptionCustom(String message) {
        super(message);
    }

    public static EntityNotFoundExceptionCustom of(Class<?> clazz, Object field) {
        return new EntityNotFoundExceptionCustom(String.format("%s with id %s does not exist", clazz.getSimpleName(), field));
    }
}

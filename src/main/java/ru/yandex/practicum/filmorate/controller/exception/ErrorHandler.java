package ru.yandex.practicum.filmorate.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.error.ErrorResponse;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Ошибка валидации объекта. Данные введены некорректно.");
        return new ErrorResponse("Ошибка валидации объекта", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleConstraintViolationException(final MethodArgumentNotValidException e) {
        log.error("Ошибка валидации аргументов метода. Данные введены некорректно.");
        return new ErrorResponse("Ошибка валидации аргументов метода", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("Ошибка поиска объекта. Объект не найден.");
        return new ErrorResponse("Ошибка поиска объекта", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Возникла внутренняя ошибка:", e);
        return new ErrorResponse("Внутренняя ошибка", e.getMessage());
    }
}

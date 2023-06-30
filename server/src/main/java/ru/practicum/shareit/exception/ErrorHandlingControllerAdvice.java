package ru.practicum.shareit.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage onNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onBadRequestException(RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage onConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        if ("uq_user_email".equals(e.getConstraintName())) {
            String message = e.getCause().getMessage();
            log.warn(message);
            return new ErrorMessage(message);
        }
        return new ErrorMessage("Неизвестная ошибка Constraint violation");
    }
}

package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorHandlingControllerAdviceTest {
    private ErrorHandlingControllerAdvice errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandlingControllerAdvice();
    }

    @Test
    void onNotFoundException() {
        var exception = new NotFoundException("message");
        var result = errorHandler.onNotFoundException(exception);
        assertNotNull(result);
        assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void onAlreadyExistException() {
        var exception = new AlreadyExistException("message");
        var result = errorHandler.onAlreadyExistException(exception);
        assertNotNull(result);
        assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void onForbiddenException() {
        var exception = new ForbiddenException("message");
        var result = errorHandler.onForbiddenException(exception);
        assertNotNull(result);
        assertEquals(exception.getMessage(), result.getError());
    }

    @Test
    void onBadRequestException() {
        var exception = new BadRequestException("message");
        var result = errorHandler.onBadRequestException(exception);
        assertNotNull(result);
        assertEquals(exception.getMessage(), result.getError());
    }
}
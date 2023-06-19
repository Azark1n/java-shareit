package ru.practicum.shareit.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(errors.toString());
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> onConstraintValidationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(errors.toString());
        return errors;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage onNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage alreadyExistException(AlreadyExistException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage forbiddenException(ForbiddenException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(BadRequestException e) {
        log.warn(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage forbiddenException2(org.hibernate.exception.ConstraintViolationException e) {
        if ("uq_user_email".equals(e.getConstraintName())) {
            String message = e.getCause().getMessage();
            log.warn(message);
            return new ErrorMessage(message);
        }
        return new ErrorMessage("Неизвестная ошибка Constraint violation");
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<String> handleSqlException(DataIntegrityViolationException ex) {
//        // Обработка ошибки
//        String errorMessage = "Произошла ошибка при выполнении SQL-запроса: " + ex.getMessage();
//        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<String> handleSQLException(SQLException ex) {
//        //DataAccessException dex = sqlExceptionHelper.convertSQLException(ex);
//        // Логика обработки возникшей ошибки
//        //((PSQLExceptio)ex).ser
//        return new ResponseEntity<>("adsfaf", HttpStatus.ACCEPTED);
//    }
}

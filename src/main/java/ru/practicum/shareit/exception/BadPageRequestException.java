package ru.practicum.shareit.exception;

public class BadPageRequestException extends RuntimeException {
    public BadPageRequestException(String message) {
        super(message);
    }
}

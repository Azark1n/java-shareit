package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorMessage {
    String error;
}

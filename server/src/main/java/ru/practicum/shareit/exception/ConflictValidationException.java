package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictValidationException extends ValidationException {
    public ConflictValidationException(String message) {
        super(message);
    }
}
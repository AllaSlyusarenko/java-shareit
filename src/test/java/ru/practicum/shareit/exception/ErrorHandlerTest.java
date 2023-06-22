package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleNotFoundException() {
        NotFoundException exception = new NotFoundException("error");
        ErrorResponse errorResponse = errorHandler.handleNotFoundException(exception);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void handleValidationException() {
        ValidationException exception = new ValidationException("error");
        ErrorResponse errorResponse = errorHandler.handleValidationException(exception);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void handleConflictValidationException() {
        ConflictValidationException exception = new ConflictValidationException("error");
        ErrorResponse errorResponse = errorHandler.handleConflictValidationException(exception);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(exception.getMessage(), errorResponse.getError());
    }
}
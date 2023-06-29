package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "ru.practicum.shareit")
public class ErrorHandler {
//    @ExceptionHandler({NotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleNotFoundException(final RuntimeException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            MissingRequestHeaderException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

//    @ExceptionHandler({ConflictValidationException.class})
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handleConflictValidationException(final RuntimeException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Unhandled exception.");
    }
}
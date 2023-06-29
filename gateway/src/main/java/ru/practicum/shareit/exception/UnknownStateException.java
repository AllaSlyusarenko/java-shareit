package ru.practicum.shareit.exception;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String unknownState) {
        super("Unknown state: " + unknownState);
    }

}
package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictValidationExceptionTest {
    @Test
    void testConstructor() {
        ConflictValidationException actualConflictValidationException = new ConflictValidationException("Не найдено");
        assertNull(actualConflictValidationException.getCause());
        assertEquals(0, actualConflictValidationException.getSuppressed().length);
        assertEquals("Не найдено", actualConflictValidationException.getMessage());
        assertEquals("Не найдено", actualConflictValidationException.getLocalizedMessage());
    }
}
package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
    @Test
    void testConstructor() {
        NotFoundException actualNotFoundException = new NotFoundException("Не найдено");
        assertNull(actualNotFoundException.getCause());
        assertEquals(0, actualNotFoundException.getSuppressed().length);
        assertEquals("Не найдено", actualNotFoundException.getMessage());
        assertEquals("Не найдено", actualNotFoundException.getLocalizedMessage());
    }
}
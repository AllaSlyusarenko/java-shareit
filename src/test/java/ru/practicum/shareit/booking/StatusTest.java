package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusTest {
    @Test
    void valuesOf() {
        Assertions.assertEquals(Status.WAITING, Status.valueOf("WAITING"));
        Assertions.assertEquals(Status.APPROVED, Status.valueOf("APPROVED"));
        Assertions.assertEquals(Status.REJECTED, Status.valueOf("REJECTED"));
        Assertions.assertEquals(Status.CANCELED, Status.valueOf("CANCELED"));
    }
}
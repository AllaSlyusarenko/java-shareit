package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShareItServerTest {

    @Test
    void main() {
        Assertions.assertDoesNotThrow(ShareItServerTest::new);
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}
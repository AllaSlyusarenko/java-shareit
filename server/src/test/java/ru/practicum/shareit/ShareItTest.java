package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShareItTest {

    @Test
    void main() {
        Assertions.assertDoesNotThrow(ShareItServer::new);
        Assertions.assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}
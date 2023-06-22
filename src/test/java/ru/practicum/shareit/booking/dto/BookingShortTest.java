package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class JsonBookingDtoTests {
    @Autowired
    private JacksonTester<BookingResponseDto> jsonBookingResponseDto;
    @Autowired
    private JacksonTester<BookingRequestDto> jsonBookingRequestDto;
    @Autowired
    private JacksonTester<BookingShort> jsonBookingShort;

    @Test
    public void testBookingShort() throws IOException {
        BookingShort bookingShort = BookingShort.builder().id(1L).bookerId(1L).build();

        JsonContent<BookingShort> result = jsonBookingShort.write(bookingShort);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

}
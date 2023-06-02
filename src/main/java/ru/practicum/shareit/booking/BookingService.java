package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    public BookingDto saveBooking(Long userId, BookingDto bookingDto);
}

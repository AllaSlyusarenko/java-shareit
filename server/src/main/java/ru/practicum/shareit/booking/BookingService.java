package ru.practicum.shareit.booking;

import ru.practicum.shareit.Generated;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Generated
public interface BookingService {
    public BookingResponseDto saveBooking(Long userId, BookingRequestDto bookingRequestDto);

    public BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean approved);

    public BookingResponseDto infoBooking(Long userId, Long bookingId);

    public List<BookingResponseDto> allBookingUser(Long userId, String state, Integer from, Integer size);

    public List<BookingResponseDto> allBookingOwner(Long ownerId, String state, Integer from, Integer size);
}

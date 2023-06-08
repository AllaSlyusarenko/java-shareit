package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingResponseDto mapToBookingResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setItem(booking.getItem());
        bookingResponseDto.setBooker(booking.getBooker());
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public static List<BookingResponseDto> mapToBookingResponseDto(Iterable<Booking> bookings) {
        List<BookingResponseDto> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(mapToBookingResponseDto(booking));
        }
        return dtos;
    }

    public static Booking mapToBooking(BookingRequestDto bookingRequestDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        return booking;
    }

    public static BookingShort mapToBookingShort(Booking booking) {
        BookingShort bookingShort = new BookingShort();
        if (booking == null) {
            return null;
        }
        bookingShort.setId(booking.getId());
        bookingShort.setBookerId(booking.getBooker().getId());
        return bookingShort;
    }
}

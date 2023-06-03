package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingResponseDto mapToBookingResponseDto(Booking booking) {
//        String dateOfStartToDto = DateTimeFormatter
//                .ofPattern("yyyy.MM.dd hh:mm:ss")
//                .withZone(ZoneOffset.UTC)
//                .format(booking.getStart());
//        String dateOfEndToDto = DateTimeFormatter
//                .ofPattern("yyyy.MM.dd hh:mm:ss")
//                .withZone(ZoneOffset.UTC)
//                .format(booking.getEnd());
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

}

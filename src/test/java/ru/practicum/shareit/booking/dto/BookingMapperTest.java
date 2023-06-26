package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class BookingMapperTest {
    @Test
    @DirtiesContext
    void mapToBookingResponseDto() {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request = Request.builder().id(1L).description("description").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).request(request).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        BookingResponseDto bookingResponseDto = BookingMapper.mapToBookingResponseDto(booking);
        //then
        Assertions.assertNotNull(bookingResponseDto);
        Assertions.assertEquals(bookingResponseDto.getId(), booking.getId());
        Assertions.assertEquals(bookingResponseDto.getStart(), booking.getStart());
        Assertions.assertEquals(bookingResponseDto.getEnd(), booking.getEnd());
        Assertions.assertEquals(bookingResponseDto.getItem(), booking.getItem());
        Assertions.assertEquals(bookingResponseDto.getStatus(), booking.getStatus());
    }

    @Test
    @DirtiesContext
    void mapToBooking() {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        User user = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).build();
        //when
        Booking booking = BookingMapper.mapToBooking(bookingRequestDto, user, item);
        //then
        Assertions.assertNotNull(booking);
        Assertions.assertEquals(booking.getStart(), bookingRequestDto.getStart());
        Assertions.assertEquals(booking.getEnd(), bookingRequestDto.getEnd());
        Assertions.assertEquals(booking.getItem(), item);
        Assertions.assertEquals(booking.getBooker(), user);
    }

    @Test
    @DirtiesContext
    void mapToBookingShort() {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request = Request.builder().id(1L).description("description").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).request(request).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        BookingShort bookingShort = BookingMapper.mapToBookingShort(booking);
        //then
        Assertions.assertNotNull(bookingShort);
        Assertions.assertEquals(bookingShort.getId(), booking.getId());
        Assertions.assertEquals(bookingShort.getBookerId(), booking.getBooker().getId());
    }
}
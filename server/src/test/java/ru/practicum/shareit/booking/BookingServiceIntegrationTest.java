package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceIntegrationTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DirtiesContext
    void saveBookingTest() {
        //given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        userRepository.save(user);
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        userRepository.save(booker);
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        itemRepository.save(item);
        //when
        BookingResponseDto bookingResponseDtoOut = bookingService.saveBooking(booker.getId(), bookingRequestDto);
        //then
        assertNotNull(bookingResponseDtoOut.getId());
        assertEquals(bookingResponseDtoOut.getStart(), bookingRequestDto.getStart());
        assertEquals(bookingResponseDtoOut.getEnd(), bookingRequestDto.getEnd());
        assertEquals(bookingResponseDtoOut.getItem().getId(), item.getId());
        assertEquals(bookingResponseDtoOut.getBooker().getName(), booker.getName());
        assertEquals(bookingResponseDtoOut.getStatus(), Status.WAITING);
    }

    @Test
    @DirtiesContext
    void approveBooking() {
        //given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        userRepository.save(user);
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        userRepository.save(booker);
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        itemRepository.save(item);
        BookingResponseDto bookingResponseDtoIn = bookingService.saveBooking(booker.getId(), bookingRequestDto);
        //when
        BookingResponseDto bookingResponseDto = bookingService.approveBooking(user.getId(), bookingResponseDtoIn.getId(), true);
        //then
        assertNotNull(bookingResponseDto.getId());
        assertNotNull(bookingResponseDto.getStart());
        assertNotNull(bookingResponseDto.getEnd());
        assertEquals(bookingResponseDto.getItem().getId(), item.getId());
        assertEquals(bookingResponseDto.getBooker().getName(), booker.getName());
        assertEquals(bookingResponseDto.getStatus(), Status.APPROVED);
    }
}
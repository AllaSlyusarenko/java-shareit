package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    void saveBooking_shouldSave_whenDataCorrect() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.saveBooking(Mockito.anyLong(), Mockito.any(BookingRequestDto.class)))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(bookingResponseDto.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().name()));
        verify(bookingService, times(1)).saveBooking(Mockito.anyLong(), Mockito.any(BookingRequestDto.class));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSave_whenDataIsWrong() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.saveBooking(Mockito.anyLong(), Mockito.any(BookingRequestDto.class)))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(post("/bookings")
                .content(objectMapper.writeValueAsString(bookingRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(bookingService, never()).saveBooking(Mockito.anyLong(), Mockito.any(BookingRequestDto.class));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldApprove_whenDataCorrect() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(bookingResponseDto.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().name()));
        verify(bookingService, times(1)).approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApprove_whenDataIsWrong() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(patch("/bookings/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("approved", "true")
        ).andExpect(status().isBadRequest());
        verify(bookingService, never()).approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    @DirtiesContext
    void getBookingById() {
    }

    @Test
    @DirtiesContext
    void allBookingUser() {
    }

    @Test
    @DirtiesContext
    void allBookingOwner() {
    }
}
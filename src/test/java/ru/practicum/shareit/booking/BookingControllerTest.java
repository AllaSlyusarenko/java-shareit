package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

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
    void getBookingById_shouldGet_whenDataCorrect() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.infoBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(bookingResponseDto.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().name()));
        verify(bookingService, times(1)).infoBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getBookingById_shouldNotGet_whenDataIsWrong() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder().id(1L).start(start).end(end).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.infoBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingResponseDto);
        //then
        mvc.perform(get("/bookings/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(bookingService, never()).infoBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGet_whenDataCorrect() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end1 = start.plusDays(1);
        LocalDateTime end2 = start.plusDays(2);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto1 = BookingResponseDto.builder().id(1L).start(start).end(end1).item(new Item())
                .booker(booker).status(status).build();
        BookingResponseDto bookingResponseDto2 = BookingResponseDto.builder().id(2L).start(start).end(end2).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.allBookingUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingResponseDto1, bookingResponseDto2));
        //then
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty())
                .andExpect(jsonPath("$[0].item").isNotEmpty())
                .andExpect(jsonPath("$[0].booker.id").value(bookingResponseDto1.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponseDto1.getStatus().name()))
                .andExpect(jsonPath("$[1].id", is(bookingResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].start").isNotEmpty())
                .andExpect(jsonPath("$[1].end").isNotEmpty())
                .andExpect(jsonPath("$[1].item").isNotEmpty())
                .andExpect(jsonPath("$[1].booker.id").value(bookingResponseDto2.getBooker().getId()))
                .andExpect(jsonPath("$[1].status").value(bookingResponseDto2.getStatus().name()));
        verify(bookingService, times(1)).allBookingUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldNotGet_whenDataIsWrong() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end1 = start.plusDays(1);
        LocalDateTime end2 = start.plusDays(2);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto1 = BookingResponseDto.builder().id(1L).start(start).end(end1).item(new Item())
                .booker(booker).status(status).build();
        BookingResponseDto bookingResponseDto2 = BookingResponseDto.builder().id(2L).start(start).end(end2).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.allBookingUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingResponseDto1, bookingResponseDto2));
        //then
        mvc.perform(get("/bookings")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "qwerty")
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "2")
        ).andExpect(status().isBadRequest());
        verify(bookingService, never()).allBookingUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGet_whenDataCorrect() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end1 = start.plusDays(1);
        LocalDateTime end2 = start.plusDays(2);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto1 = BookingResponseDto.builder().id(1L).start(start).end(end1).item(new Item())
                .booker(booker).status(status).build();
        BookingResponseDto bookingResponseDto2 = BookingResponseDto.builder().id(2L).start(start).end(end2).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.allBookingOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingResponseDto1, bookingResponseDto2));
        //then
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty())
                .andExpect(jsonPath("$[0].item").isNotEmpty())
                .andExpect(jsonPath("$[0].booker.id").value(bookingResponseDto1.getBooker().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponseDto1.getStatus().name()))
                .andExpect(jsonPath("$[1].id", is(bookingResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].start").isNotEmpty())
                .andExpect(jsonPath("$[1].end").isNotEmpty())
                .andExpect(jsonPath("$[1].item").isNotEmpty())
                .andExpect(jsonPath("$[1].booker.id").value(bookingResponseDto2.getBooker().getId()))
                .andExpect(jsonPath("$[1].status").value(bookingResponseDto2.getStatus().name()));
        verify(bookingService, times(1)).allBookingOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldNotGet_whenDataIsWrong() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end1 = start.plusDays(1);
        LocalDateTime end2 = start.plusDays(2);
        Status status = Status.WAITING;
        User booker = new User(1L, "Name", "name@ya.ru");
        //given
        BookingResponseDto bookingResponseDto1 = BookingResponseDto.builder().id(1L).start(start).end(end1).item(new Item())
                .booker(booker).status(status).build();
        BookingResponseDto bookingResponseDto2 = BookingResponseDto.builder().id(2L).start(start).end(end2).item(new Item())
                .booker(booker).status(status).build();
        //when
        when(bookingService.allBookingOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingResponseDto1, bookingResponseDto2));
        //then
        mvc.perform(get("/bookings/owner")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "qwerty")
                .param("state", "ALL")
                .param("from", "0")
                .param("size", "2")
        ).andExpect(status().isBadRequest());
        verify(bookingService, never()).allBookingOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }
}
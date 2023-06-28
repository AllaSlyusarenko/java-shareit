package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

//    @PostMapping
//    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
//                                           @RequestBody @Valid BookingRequestDto requestDto) {
//        log.info("Creating booking {}, userId={}", requestDto, userId);
//        return bookingClient.bookItem(userId, requestDto);
//    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }



    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader(USER_ID)  @Positive Long userId,
                                          @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Creating booking {}, userId={}", bookingRequestDto, userId);
        return bookingClient.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId,
                                             @RequestParam @NotBlank String approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("Получение информации о бронировании по id");
        return bookingService.infoBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> allBookingUser(@RequestHeader(USER_ID) @Positive Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка всех бронирований пользователя");
        return bookingService.allBookingUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> allBookingOwner(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка всех бронирований владельца");
        return bookingService.allBookingOwner(ownerId, state, from, size);
    }
}

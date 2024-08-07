package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader(USER_ID) @Positive Long userId,
                                              @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Creating booking {}, userId={}", bookingRequestDto, userId);
        return bookingClient.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID) @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId,
                                                 @RequestParam("approved") Boolean approved) {
        log.info("Подтверждение или отклонение запроса на бронирование с Id {}", bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID) @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId) {
        log.info("Получение информации о бронировании по Id {}", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> allBookingUser(@RequestHeader(USER_ID) @Positive Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка всех бронирований пользователя с Id {}", userId);
        return bookingClient.allBookingUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> allBookingOwner(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка всех бронирований владельца с Id {}", ownerId);
        return bookingClient.allBookingOwner(ownerId, state, from, size);
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto saveBooking(@RequestHeader(USER_ID) Long userId,
                                          @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Создание новой вещи пользователем {}", userId);
        return bookingService.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        log.info("Подтверждение или отклонение запроса на бронирование с Id {}", bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получение информации о бронировании по Id {}", bookingId);
        return bookingService.infoBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> allBookingUser(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Получение списка всех бронирований пользователя с Id {}", userId);
        return bookingService.allBookingUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> allBookingOwner(@RequestHeader(USER_ID) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Получение списка всех бронирований владельца с Id {}", ownerId);
        return bookingService.allBookingOwner(ownerId, state, from, size);
    }
}
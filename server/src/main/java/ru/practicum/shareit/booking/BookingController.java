package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto saveBooking(@RequestHeader(USER_ID)   Long userId,
                                           @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Создание новой вещи");
        return bookingService.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(USER_ID)  Long userId,
                                             @PathVariable  Long bookingId,
                                             @RequestParam String approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID)  Long userId,
                                             @PathVariable  Long bookingId) {
        log.info("Получение информации о бронировании по id");
        return bookingService.infoBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> allBookingUser(@RequestHeader(USER_ID)  Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(value = "from", defaultValue = "0")  Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10")  Integer size) {
        log.info("Получение списка всех бронирований пользователя");
        return bookingService.allBookingUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> allBookingOwner(@RequestHeader(USER_ID)  Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(value = "from", defaultValue = "0")  Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10")  Integer size) {
        log.info("Получение списка всех бронирований владельца");
        return bookingService.allBookingOwner(ownerId, state, from, size);
    }
}
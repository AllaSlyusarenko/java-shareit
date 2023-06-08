package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Создание новой вещи");
        return bookingService.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam String approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Получение информации о бронировании по id");
        return bookingService.infoBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> allBookingUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение списка всех бронирований пользователя");
        return bookingService.allBookingUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> allBookingOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение списка всех бронирований владельца");
        return bookingService.allBookingOwner(ownerId, state);
    }
}
